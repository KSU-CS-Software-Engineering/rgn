package ksu.rgn.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
abstract class Job {

    public abstract void run(EntityManager em);
    public boolean mergeActions(Job j) {
        return false;
    }

    @Override
    public String toString() {
        return "Job." + getClass();
    }

    static class SimplePersist extends Job {
        private final Object o;
        public SimplePersist(Object o) {
            this.o = o;
            if (this.o == null) {
                throw new NullPointerException("Object to persist cannot be null");
            }
        }

        @Override
        public void run(EntityManager em) {
            final EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(o);
            et.commit();
        }

        @Override
        public boolean mergeActions(Job j) {
            if (j instanceof SimplePersist) {
                return ((SimplePersist) j).o == o;
            }
            return false;
        }
    }

    static class ActionPersist extends Job {
        private final Object o;
        private final Runnable a;
        private final ArrayList<Runnable> prePersist = new ArrayList<>();
        public ActionPersist(Runnable action, Object o) {
            this.o = o;
            this.a = action;
            if (this.o == null) {
                throw new NullPointerException("Object to persist cannot be null");
            }
        }

        @Override
        public void run(EntityManager em) {
            final EntityTransaction et = em.getTransaction();
            if (a != null) prePersist.add(a);
            prePersist.forEach(Runnable::run);
            et.begin();
            em.persist(o);
            et.commit();
        }

        @Override
        public boolean mergeActions(Job j) {
            if (j instanceof ActionPersist) {
                if (((ActionPersist) j).o == o) {
                    prePersist.addAll(((ActionPersist) j).prePersist);
                    if (a != null) prePersist.add(((ActionPersist) j).a);
                    return true;
                }
            } else if (j instanceof SimplePersist) {
                return (((SimplePersist) j).o == o);
            }
            return false;
        }
    }

    static class SimpleDrop extends Job {
        private final Object o;
        public SimpleDrop(Object o) {
            this.o = o;
            if (this.o == null) {
                throw new NullPointerException("Object to drop cannot be null");
            }
        }

        @Override
        public void run(EntityManager em) {
            final EntityTransaction et = em.getTransaction();
            et.begin();
            em.remove(o);
            et.commit();
        }

    }

    static class Query extends Job {
        private final String q;
        private final Class<?> c;
        private final Consumer<List> cb;
        public Query(String qlQuery, Class<?> resultClass, Consumer<List> cb) {
            this.q = qlQuery;
            this.c = resultClass;
            this.cb = cb;
        }

        @Override
        public void run(EntityManager em) {
            List result = em.createQuery(q, c).getResultList();
            if (cb != null) cb.accept(result);
        }
    }
}
