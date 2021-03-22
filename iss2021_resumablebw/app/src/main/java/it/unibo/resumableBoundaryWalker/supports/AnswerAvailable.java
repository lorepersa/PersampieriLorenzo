/**
 AnswerAvailable
 ===============================================================
 Utility class to capture information about the reply to a request
 sent by the server over the ws connection.
 The put operation is called by onMessage
 ===============================================================
 */
package it.unibo.resumableBoundaryWalker.supports;

public class AnswerAvailable {
    private String  answer  = null;

    public synchronized void put(String info, String move) {
        synchronized (this) {
            answer = info;
        }
        notify();
    }

    public synchronized String get( ) {
        String currAnswer;
        do {
            synchronized (this) {
                currAnswer = answer;
            }
            if(currAnswer == null) {
                try { wait(); }
                catch (InterruptedException e) { }
                finally { }
            }
        } while(currAnswer == null);

        String myAnswer;
        synchronized (this) {
            myAnswer = answer;
            answer = null;
        }

        return myAnswer;
    }
}