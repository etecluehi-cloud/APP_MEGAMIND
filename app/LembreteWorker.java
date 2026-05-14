
public class LembreteWorker extends Worker {
    public LembreteWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        // Pega qual notificação enviar — alterna entre 0 e 1
        int turno = context.getSharedPreferences("config", Context.MODE_PRIVATE)
                .getInt("turno_notificacao", 0);

        String titulo, mensagem;

        if (turno == 0) {
            // Ideia 1 — Lembrete de estudo
            titulo = "📚 Hora de estudar!";
            mensagem = "Você tem conteúdos te esperando no MegaMind. Bora estudar!";
        } else {
            // Ideia 2 — Lembrete de streak
            Gamificacao g = new Gamificacao(context);
            int streak = g.getStreak();
            titulo = "🔥 Não perca sua sequência!";
            mensagem = "Você está com " + streak + " dias de estudo seguidos. Continue assim!";
        }

        // Alterna para a próxima notificação
        context.getSharedPreferences("config", Context.MODE_PRIVATE)
                .edit()
                .putInt("turno_notificacao", turno == 0 ? 1 : 0)
                .apply();

        // Envia a notificação
        enviarNotificacao(context, titulo, mensagem);

        return Result.success();
    }

    private void enviarNotificacao(Context context, String titulo, String mensagem) {
        String canalId = "megamind_canal";
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Cria o canal (obrigatório no Android 8+)
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
                .setSmallIcon(R.drawable.ic_user) // troque por um ícone do seu app
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mensagem))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }
}
