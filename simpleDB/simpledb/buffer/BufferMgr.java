package simpledb.buffer;

import simpledb.file.*;

/**
Â * The publicly-accessible buffer manager. A buffer manager wraps a basic buffer
Â * manager, and provides the same methods. The difference is that the methods
Â * {@link #pin(Block) pin} and {@link #pinNew(String, PageFormatter) pinNew}
Â * will never return null. If no buffers are currently available, then the
Â * calling thread will be placed on a waiting list. The waiting threads are
Â * removed from the list when a buffer becomes available. If a thread has been
Â * waiting for a buffer for an excessive amount of time (currently, 10 seconds)
Â * then a {@link BufferAbortException} is thrown.
Â *Â 
Â * @author Edward Sciore
Â */
public class BufferMgr {
	private static final long MAX_TIME = 10000; // 10 seconds
	public BasicBufferMgr bufferMgr;

	/**
	* Creates a new buffer manager having the specified number of buffers. This
	* constructor depends on both the {@link FileMgr} and
	* {@link simpledb.log.LogMgr LogMgr} objects that it gets from the class
	* {@link simpledb.server.SimpleDB}. Those objects are created during system
	* initialization. Thus this constructor cannot be called until
	* {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or is called
	* first.
	*Â 
	* @param numbuffers
	*Â  Â  Â  Â  Â  Â  the number of buffer slots to allocate
	*/
	public BufferMgr(int numbuffers) {
		bufferMgr = new BasicBufferMgr(numbuffers);
	}

	// Returns info for each buffer in the pool.
	public String toString() {
		return bufferMgr.toString();
	}

	/**
	* Pins a buffer to the specified block, potentially waiting until a buffer
	* becomes available. If no buffer becomes available within a fixed time
	* period, then a {@link BufferAbortException} is thrown.
	*Â 
	* @param blk
	*Â  Â  Â  Â  Â  Â  a reference to a disk block
	* @return the buffer pinned to that block
	* @throws BufferAbortException
	*/
	public synchronized Buffer pin(Block blk) {

		try {
			long timestamp = System.currentTimeMillis();
			Buffer buff = bufferMgr.pin(blk);
			while (buff == null && !waitingTooLong(timestamp)) {
				wait(MAX_TIME);
				buff = bufferMgr.pin(blk);
			}
			if (buff == null)
				throw new BufferAbortException();
			return buff;
		} catch (InterruptedException e) {
			throw new BufferAbortException();

		}
	}

	/**
	* Pins a buffer to a new block in the specified file, potentially waiting
	* until a buffer becomes available. If no buffer becomes available within a
	* fixed time period, then a {@link BufferAbortException} is thrown.
	*Â 
	* @param filename
	*Â  Â  Â  Â  Â  Â  the name of the file
	* @param fmtr
	*Â  Â  Â  Â  Â  Â  the formatter used to initialize the page
	* @return the buffer pinned to that block
	* @throws BufferAbortException
	*/
	public synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
		try {
			long timestamp = System.currentTimeMillis();
			Buffer buff = bufferMgr.pinNew(filename, fmtr);
			while (buff == null && !waitingTooLong(timestamp)) {
				wait(MAX_TIME);
				buff = bufferMgr.pinNew(filename, fmtr);
			}
			if (buff == null)
				throw new BufferAbortException();
			return buff;
		} catch (InterruptedException e) {
			throw new BufferAbortException();
		}
	}

	/**
	* Unpins the specified buffer. If the buffer's pin count becomes 0, then
	* the threads on the wait list are notified.
	*Â 
	* @param buff
	*Â  Â  Â  Â  Â  Â  the buffer to be unpinned
	*/
	public synchronized void unpin(Buffer buff) {
		bufferMgr.unpin(buff);
		if (!buff.isPinned())
			notifyAll();
	}

	/**
	* Flushes the dirty buffers modified by the specified transaction.
	*Â 
	* @param txnum
	*Â  Â  Â  Â  Â  Â  the transaction's id number
	*/
	public void flushAll(int txnum) {
		bufferMgr.flushAll(txnum);
	}

	/**
	* Returns the number of available (ie unpinned) buffers.
	* @return the number of available buffers
	*/
	public int available() {
		return bufferMgr.available();
	}

	private boolean waitingTooLong(long starttime) {
		return System.currentTimeMillis() - starttime > MAX_TIME;
	}

	/** Â 
	* Determines whether the map has a mapping from Â 
	* the block to some buffer. Â 
	* @paramblk the block to use as a key Â 
	* @return true if there is a mapping; false otherwise Â 
	*/ 
	public boolean containsMapping(Block blk) {
		return bufferMgr.containsMapping(blk);
	}

	/**
	* Returns the buffer that the map maps the specified block to.
	* @paramblk the block to use as a key
	* @return the buffer mapped to if there is a mapping; null otherwise
	*/
	public Buffer getMapping(Block blk) {
		return bufferMgr.getMapping(blk);
	}
}

