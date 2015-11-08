package events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dimitri on 07/11/2015.
 */
public class LockSetObject {
    public class LockSetEvent extends EventObject {

        private int type;

        public LockSetEvent(Object source, int type) {
            super(source);
            this.type = type;
        }

        public int getType(){
            return this.type;
        }

    }

    public interface LockSetEventListener extends EventListener {
        void onLockSet(LockSetEvent ue);
    }


    public List<LockSetEventListener> listeners = new ArrayList<>();
    public Object src;
    public int type;

    public LockSetObject(Object src, int type){
        this.src = src;
        this.type = type;
    }

    public synchronized void addLockSetEventListener(LockSetEventListener l) {
        listeners.add(l);
    }

    public synchronized void removeLockSetEventListener(LockSetEventListener l) {
        listeners.remove(l);
    }

    public synchronized void fireLockSetEvent() {
        LockSetEvent ue = new LockSetEvent(src, type);
        Iterator iListeners = listeners.iterator();
        while(iListeners.hasNext()) {
            ((LockSetEventListener) iListeners.next()).onLockSet(ue);
        }
    }
}
