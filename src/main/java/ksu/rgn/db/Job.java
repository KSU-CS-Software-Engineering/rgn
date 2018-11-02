package ksu.rgn.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
abstract class Job {

    public abstract void run(EntityManager em);
    public boolean makesObsolete(Job j) {
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
        }

        @Override
        public void run(EntityManager em) {
            final EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(o);
            et.commit();
        }

        @Override
        public boolean makesObsolete(Job j) {
            if (j instanceof SimplePersist) {
                return ((SimplePersist) j).o == o;
            }
            return false;
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
