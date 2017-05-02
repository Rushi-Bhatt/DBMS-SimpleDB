package simpledb.buffer;

import java.util.Date;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import simpledb.file.Block;
import simpledb.file.FileMgr;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * 
 * @author Edward Sciore
 * 
 */
public class BasicBufferMgr {
	public Buffer[] bufferpool;
	private int numAvailable;
	public HashMap<Block, Buffer> bufferPoolMap;
	public LinkedList<Buffer> availableBuf;

	/**
	 * Creates a buffer manager having the specified number of buffer slots.
	 * This constructor depends on both the {@link FileMgr} and
	 * {@link simpledb.log.LogMgr LogMgr} objects that it gets from the class
	 * {@link simpledb.server.SimpleDB}. Those objects are created during system
	 * initialization. Thus this constructor cannot be called until
	 * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or is called
	 * first.
	 * 
	 * @param numbuffs
	 *            the number of buffer slots to allocate
	 */
	BasicBufferMgr(int numbuffs) {
		bufferpool = new Buffer[numbuffs];
		numAvailable = numbuffs;
		bufferPoolMap = new HashMap<Block, Buffer>(numbuffs);
		availableBuf = new LinkedList<Buffer>();
		for (int i = 0; i < numbuffs; i++) {
			bufferpool[i] = new Buffer(i);
			availableBuf.add(bufferpool[i]);
		}
	}
	
	/**
	 * Flushes the dirty buffers modified by the specified transaction.
	 * 
	 * @param txnum
	 *            the transaction's id number
	 */
	synchronized void flushAll(int txnum) {
		Buffer buff = null;
		for (Block key : bufferPoolMap.keySet()) {
			buff = bufferPoolMap.get(key);
			if (buff.isModifiedBy(txnum)) {
				buff.flush();
			}
		}
	}

	/**
	 * Pins a buffer to the specified block. If there is already a buffer
	 * assigned to that block then that buffer is used; otherwise, an unpinned
	 * buffer from the pool is chosen. Returns a null value if there are no
	 * available buffers.
	 * 
	 * @param blk
	 *            a reference to a disk block
	 * @return the pinned buffer
	 */
	synchronized Buffer pin(Block blk) {
		Buffer buff = findExistingBuffer(blk);
		if (buff == null) {
			buff = chooseUnpinnedBuffer();
			if (buff == null) {
				return null;
			}
			if (buff.block() != null) {
				bufferPoolMap.remove(buff.block());
			}
			buff.assignToBlock(blk);
		}
		if (!buff.isPinned())
			numAvailable--;
		bufferPoolMap.put(blk, buff);
		buff.pin();
		return buff;
	}

	/**
	 * Allocates a new block in the specified file, and pins a buffer to it.
	 * Returns null (without allocating the block) if there are no available
	 * buffers.
	 * 
	 * @param filename
	 *            the name of the file
	 * @param fmtr
	 *            a page formatter object, used to format the new block
	 * @return the pinned buffer
	 */
	synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
		Buffer buff = chooseUnpinnedBuffer();
		if (buff == null) {
			throw new BufferAbortException();
		}
		buff.assignToNew(filename, fmtr);
		bufferPoolMap.put(buff.block(), buff);
		numAvailable--;
		buff.pin();
		return buff;
	}

	/**
	 * Unpins the specified buffer.
	 * 
	 * @param buff
	 *            the buffer to be unpinned
	 */
	synchronized void unpin(Buffer buff) {
		buff.unpin();
		if (!buff.isPinned())
			numAvailable++;
	}

	/**
	 * Returns the number of available (i.e. unpinned) buffers.
	 * 
	 * @return the number of available buffers
	 */
	int available() {
		return numAvailable;
	}

	private Buffer findExistingBuffer(Block blk) {
		if (bufferPoolMap.get(blk) != null) {
			return bufferPoolMap.get(blk);
		} else {
			return null;
		}
	}

	private Buffer chooseUnpinnedBuffer() {

		int min = Integer.MIN_VALUE;
		Buffer curr = null;
		Buffer buff = null;

		if (!availableBuf.isEmpty()) {
			buff = availableBuf.getFirst();
			availableBuf.removeFirst();
			System.out.println("New Buffer Allocated ------ "+buff.getBufferIndex());
			return buff;
		}

		for (Block key : bufferPoolMap.keySet()) {
			curr = bufferPoolMap.get(key);
			if (!curr.isPinned() && curr.modifiedBy >= 0)
				{
					if(min < curr.logSequenceNumber && curr.logSequenceNumber >= 0)
					{
						buff = curr;
						min = buff.logSequenceNumber;
					}
				}
			}

		if (buff == null) {
			for (Block key : bufferPoolMap.keySet()) {
				curr = bufferPoolMap.get(key);
				if (!curr.isPinned()) {
					buff = curr;
					System.out.println("Buffer which is choosen for replacement ------- "+buff.getBufferIndex());
					return buff;
				}
			}
		}
		else
		{
			System.out.println("Buffer which is choosen for replacement ------- "+buff.getBufferIndex());
		}
		return buff;

	}

	public void getStatistics() {
		Buffer buff = null;
		for (Block key : bufferPoolMap.keySet()) {
			buff = bufferPoolMap.get(key);
			System.out.println("Statistics for the Buffer ------- "+buff.getBufferIndex());
			System.out.println("Read Count -----------------------"+buff.getReadCount());
			System.out.println("Write Count ----------------------"+buff.getWriteCount());
			System.out.println("Log Sequence Number --------------"+buff.logSequenceNumber);
			System.out.println();
		}
		System.out.println("-----------------------------------------------------------------");
	}

	/**  
	* Determines whether the map has a mapping from  
	* the block to some buffer.  
	* @paramblk the block to use as a key  
	* @return true if there is a mapping; false otherwise  
	*/  
	public boolean containsMapping(Block blk) {  
		return bufferPoolMap.containsKey(blk);  
	} 
	/**  
	* Returns the buffer that the map maps the specified block to.  
	* @paramblk the block to use as a key  
	* @return the buffer mapped to if there is a mapping; null otherwise  
	*/  
	public Buffer getMapping(Block blk) {  
		return bufferPoolMap.get(blk);  
	} 

}
