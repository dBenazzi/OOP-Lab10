package it.unibo.oop.lab.reactivegui03;

public class AnotherConcurrentGUI extends ConcurrentGUI {
    AnotherConcurrentGUI() {
        super();
        final StopAgent stopAgent = new StopAgent();
        new Thread(stopAgent).start();
    }

    private class StopAgent implements Runnable {
        protected static final long WAIT_TIME = 10_000L;

        @Override
        public void run() {
            try {
                Thread.sleep(StopAgent.WAIT_TIME);
                AnotherConcurrentGUI.this.agent.stopCounting();
                up.setEnabled(false);
                down.setEnabled(false);
                stop.setEnabled(false);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } 
}
