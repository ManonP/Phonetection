package events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dimitri on 07/11/2015.
 */
public class WrongLockSetObject {
    public class WrongLockSetEvent extends EventObject {

        public static final int MOTION_MODE = 0;
        public static final int CHARGER_MODE = 1;
        public static final int SIM_MODE = 2;
        public static final int SMS_MODE = 3;

        private int type;

        public WrongLockSetEvent(Object source, int type) {
            super(source);
            this.type = type;
        }

        public int getType(){
            return this.type;
        }

    }

    public interface WrongLockSetEventListener extends EventListener {
        void onWrongLockSet(WrongLockSetEvent ue);
    }


    public List<WrongLockSetEventListener> listeners = new ArrayList<>();
    public Object src;
    public int type;

    public WrongLockSetObject(Object src, int type){
        this.src = src;
        this.type = type;
    }

    public synchronized void addWrongLockSetEventListener(WrongLockSetEventListener l) {
        listeners.add(l);
    }

    public synchronized void removeWrongLockSetEventListener(WrongLockSetEventListener l) {
        listeners.remove(l);
    }

    public synchronized void fireWrongLockSetEvent() {
        WrongLockSetEvent ue = new WrongLockSetEvent(src, type);
        Iterator iListeners = listeners.iterator();
        while(iListeners.hasNext()) {
            ((WrongLockSetEventListener) iListeners.next()).onWrongLockSet(ue);
        }
    }
}