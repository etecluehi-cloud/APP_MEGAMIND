package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
            titulo = "📚 Hora de estudar!";
            mensagem = "Você tem conteúdos te esperando no MegaMind. Bora estudar!";
        } else {
            // Verifica se o usuário já abriu o app hoje
            String ultimoLogin = context.getSharedPreferences("config", Context.MODE_PRIVATE)
                    .getString("ultimo_login", "");

            String hoje = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(new java.util.Date());

            if (ultimoLogin.equals(hoje)) {
                return Result.success(); // já entrou hoje, não envia
            }

            Gamificacao g = new Gamificacao(context);
            int streak = g.getStreak();
            titulo = "🔥 Não perca sua sequência!";
            mensagem = "Você está com " + streak + " dias de estudo seguidos. Abra o app antes da meia-noite!";
        }

        context.getSharedPreferences("config", Context.MODE_PRIVATE)
                .edit()
                .putInt("turno_notificacao", turno == 0 ? 1 : 0)
                .apply();

        enviarNotificacao(context, titulo, mensagem);

        return Result.success();
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