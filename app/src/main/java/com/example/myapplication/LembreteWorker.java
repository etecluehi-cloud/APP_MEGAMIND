package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LembreteWorker extends Worker {

    public LembreteWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        int turno = context.getSharedPreferences("config", Context.MODE_PRIVATE)
                .getInt("turno_notificacao", 0);

        String titulo, mensagem;

        if (turno == 0) {
            // ── Notificação da manhã: lembrete genérico ───────────────────────
            titulo   = "📚 Hora de estudar!";
            mensagem = "Você tem conteúdos te esperando no MegaMind. Bora estudar!";

        } else {
            // ── Notificação da noite: só envia se o usuário NÃO entrou hoje ───
            String ultimoLogin = context.getSharedPreferences("config", Context.MODE_PRIVATE)
                    .getString("ultimo_login", "");

            String hoje = new java.text.SimpleDateFormat(
                    "dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(new java.util.Date());

            if (ultimoLogin.equals(hoje)) {
                return Result.success(); // já entrou hoje, não envia
            }

            // ── Busca o streak direto do Firestore (sem instanciar Activity) ──
            long streak = buscarStreakDoFirestore();

            titulo   = "🔥 Não perca sua sequência!";
            mensagem = "Você está com " + streak + " dias de estudo seguidos. "
                    + "Abra o app antes da meia-noite!";
        }

        // Alterna o turno para a próxima execução
        context.getSharedPreferences("config", Context.MODE_PRIVATE)
                .edit()
                .putInt("turno_notificacao", turno == 0 ? 1 : 0)
                .apply();

        enviarNotificacao(context, titulo, mensagem);
        return Result.success();
    }

    /**
     * Busca o campo "streak" do usuário logado no Firestore de forma síncrona.
     * Workers rodam em background thread, então Tasks.await() é seguro aqui.
     * Retorna 0 se não conseguir buscar.
     */
    private long buscarStreakDoFirestore() {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) return 0;

            DocumentSnapshot doc = Tasks.await(
                    FirebaseFirestore.getInstance()
                            .collection("usuarios")
                            .document(user.getUid())
                            .get()
            );

            Long streak = doc.getLong("streak");
            return streak != null ? streak : 0;

        } catch (Exception e) {
            return 0; // se falhar, mostra 0 dias e não quebra
        }
    }

    private void enviarNotificacao(Context context, String titulo, String mensagem) {
        String canalId = "megamind_canal";
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    canalId,
                    "Lembretes MegaMind",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            canal.setDescription("Lembretes de estudo e streak");
            manager.createNotificationChannel(canal);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canalId)
                .setSmallIcon(R.drawable.ic_user)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mensagem))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }
}