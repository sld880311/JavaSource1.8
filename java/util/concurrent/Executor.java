/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

/**
 * An object that executes submitted {@link Runnable} tasks. This
 * interface provides a way of decoupling task submission from the
 * mechanics of how each task will be run, including details of thread
 * use, scheduling, etc.  An {@code Executor} is normally used
 * instead of explicitly creating threads. For example, rather than
 * invoking {@code new Thread(new(RunnableTask())).start()} for each
 * of a set of tasks, you might use:
 * 
 * Executor��һ�������ύ����Ķ���Executor�ӿ��ṩһ�ֽ���������ύ��ʽ��
 * �����ύ��ʽԴ��ÿ��������α�ִ�еĻ��ƣ������̵߳�ʹ��ϸ�ں͵��ȡ�
 * ����ӿ�ͨ��������ȷ�����̵߳������ʽ
 *
 * <pre>
 * Executor executor = <em>anExecutor</em>;
 * executor.execute(new RunnableTask1());
 * executor.execute(new RunnableTask2());
 * ...
 * </pre>
 *
 * However, the {@code Executor} interface does not strictly
 * require that execution be asynchronous. In the simplest case, an
 * executor can run the submitted task immediately in the caller's
 * thread:
 * Executor�ӿڲ����ϸ�Ҫ�������첽ִ�У���ʱ�����ڵ�ǰ���õ��߳�������ִ�С�
 *
 *  <pre> {@code
 * class DirectExecutor implements Executor {
 *   public void execute(Runnable r) {
 *     r.run();
 *   }
 * }}</pre>
 *
 * More typically, tasks are executed in some thread other
 * than the caller's thread.  The executor below spawns a new thread
 * for each task.
 * 
 * ������͵ĳ�����,����һЩ�߳�ִ�У�
 * �������ڵ�ǰ�߳���,����������У�Ϊÿһ������Ͳ�����һ���µ��̡߳�
 *
 *  <pre> {@code
 * class ThreadPerTaskExecutor implements Executor {
 *   public void execute(Runnable r) {
 *     new Thread(r).start();
 *   }
 * }}</pre>
 *
 * Many {@code Executor} implementations impose some sort of
 * limitation on how and when tasks are scheduled.  The executor below
 * serializes the submission of tasks to a second executor,
 * illustrating a composite executor.
 *һЩExecutor��ʵ��ǿ��ĳ�ֹ��������ʱ����α����ȵ����ƣ�
 *�±ߵĴ��л������񽻸��ڶ���executorȥִ��
 *  <pre> {@code
 * class SerialExecutor implements Executor {
 *   final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
 *   final Executor executor;
 *   Runnable active;
 *
 *   SerialExecutor(Executor executor) {
 *     this.executor = executor;
 *   }
 *
 *   public synchronized void execute(final Runnable r) {
 *     tasks.offer(new Runnable() {
 *       public void run() {
 *         try {
 *           r.run();
 *         } finally {
 *           scheduleNext();
 *         }
 *       }
 *     });
 *     if (active == null) {
 *       scheduleNext();
 *     }
 *   }
 *
 *   protected synchronized void scheduleNext() {
 *     if ((active = tasks.poll()) != null) {
 *       executor.execute(active);
 *     }
 *   }
 * }}</pre>
 *
 * The {@code Executor} implementations provided in this package
 * implement {@link ExecutorService}, which is a more extensive
 * interface.  The {@link ThreadPoolExecutor} class provides an
 * extensible thread pool implementation. The {@link Executors} class
 * provides convenient factory methods for these Executors.
 *�ڵ�ǰ�����ṩ��һЩʵ��ExecutorService�ӿڵ�ʵ�֣�����ӿ���һ����ԱȽϹ㷺�Ľӿڣ�
 *ThreadPoolExecutor�������ṩ��һ������չ���̳߳�ʵ�֡�
 *Execturos��̬���ṩһЩ����Ĺ�������ȥ������ͬ��Executor
 * <p>Memory consistency effects: Actions in a thread prior to
 * submitting a {@code Runnable} object to an {@code Executor}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * its execution begins, perhaps in another thread.
 * �ڴ�һ����Ӱ�죺�ڵ�ǰ�̵߳Ķ���Ҫ�������ύ��Executor���������
 * ������������ʼִ���е�ʱ���������һ���߳����ˡ�
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface Executor {

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *ִ�и���������������δ����ĳ��ʱ��㣬
     *������������һ���µ��߳��������һ���̳߳��Ҳ�п��ܾ���ǰ�����߳��С�
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    void execute(Runnable command);
}
