package main.java.net.preibisch.distribution.algorithm.controllers.items;

import java.util.Map;

import main.java.net.preibisch.distribution.algorithm.blockmanager.block.BlockInfo;
import net.imglib2.util.Util;

public class BlocksMetaData {
	private long[] dimensions;
	private long[] blocksize;
	private Map<Integer,BlockInfo> blocksInfo;
	

	public BlocksMetaData(Map<Integer,BlockInfo> blocksInfo,long[] bsizes, long[] dimensions) {
		super();
		this.dimensions = dimensions;
		this.blocksize = bsizes;
		this.blocksInfo = blocksInfo;
	}
	
	public Map<Integer, BlockInfo> getBlocksInfo() {
		return blocksInfo;
	}
	
	public void setBlocksInfo(Map<Integer, BlockInfo> blocksInfo) {
		this.blocksInfo = blocksInfo;
	}

	public long[] getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(long[] blocksize) {
		this.blocksize = blocksize;
	}

	public long[] getDimensions() {
		return dimensions;
	}

	public void setDimensions(long[] dimensions) {
		this.dimensions = dimensions;
	}
	
	
	@Override
	public String toString() {
		String str = "\nMetaData: total:"+blocksInfo.size()+
				" dims:"+ Util.printCoordinates(dimensions)
				+" blocks"+Util.printCoordinates(blocksize)+"\n";
		String elms = "";
		for (int i = 0; i<blocksInfo.size();i++) {
			elms = elms+ i+"-"+ blocksInfo.get(i).toString()+" \n";
		}
//		String elm1 = blocksInfo.get(1).toString()+" \n";
		return str+elms;
	}
	
}
