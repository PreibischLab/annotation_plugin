package net.preibisch.fusiontask.task;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.RawCompression;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mpicbg.spim.data.SpimDataException;
import mpicbg.spim.data.sequence.ViewId;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.preibisch.distribution.algorithm.blockmanager.block.BasicBlockInfo;
import net.preibisch.distribution.algorithm.clustering.scripting.TaskType;
import net.preibisch.distribution.algorithm.controllers.items.BlocksMetaData;
import net.preibisch.distribution.io.img.XMLFile;
import net.preibisch.distribution.io.img.n5.N5File;
import net.preibisch.distribution.tools.Tools;
import net.preibisch.mvrecon.fiji.spimdata.boundingbox.BoundingBox;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MainJob implements Callable<Void> {
	@Option(names = { "-t", "--task" }, required = false, description = "The path of the Data")
	private String task;

	@Option(names = { "-o", "--output" }, required = false, description = "The path of the Data")
	private String output;

	@Option(names = { "-i", "--input" }, required = false, description = "The path of the Data")
	private String input;

	@Option(names = { "-m", "--meta" }, required = false, description = "The path of the MetaData file")
	private String metadataPath;

	@Option(names = { "-id" }, required = false, description = "The id of block")
	private Integer id;

	public MainJob() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void call() throws Exception {
		try {id = id -1; 
		
		TaskType type = TaskType.of(task);
		switch (type) {
		case PREPARE:
			generateN5(input, metadataPath, output, id);
			return null;
		case PROCESS:
			blockTask(input, metadataPath, output, id);
			return null;

		default:
			System.out.println("Error");
			throw new Exception("Specify task!");
		}
		} catch(Exception e) {
			System.out.println("Error");
			throw new Exception("Specify task!");
		}
//			MyLogger.log.info("Block " + id + " saved !");	
	}
	
	public static void blockTask(String inputPath, String metadataPath, String outputPath, int id) {
		try {
			System.out.println("Start process: "+id);
//			XMLFile inputFile = XMLFile.XMLFile(inputPath);
			BlocksMetaData md = BlocksMetaData.fromJson(metadataPath);
			System.out.println("Got metadata !");
			BasicBlockInfo binfo = md.getBlocksInfo().get(id);
			System.out.println("Got block info !");
			BoundingBox bb = new BoundingBox(Util.long2int(binfo.getMin()), Util.long2int(binfo.getMax()));
			System.out.println("Bounding box created: "+bb.toString());
			List<ViewId> viewIds = md.getViewIds() ;
			System.out.println("get view ids ");

//			XMLFile inputFile = XMLFile.XMLFile(inputPath, bb, md.getDownsample() , viewIds);
			XMLFile inputFile = XMLFile.XMLFile(inputPath);
			RandomAccessibleInterval<FloatType> block = inputFile.fuse(bb);
			N5File outputFile = N5File.open(outputPath);
			outputFile.saveBlock(block, binfo.getGridOffset());
			System.out.println("Task finished "+id);
		} catch (SpimDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateN5(String inputPath, String metadataPath, String outputPath, int id) {
		try {
			BlocksMetaData md = BlocksMetaData.fromJson(metadataPath);
			long[] dims = md.getDimensions();
			int blockUnit = md.getBlockUnit();
			N5File outputFile = new N5File(outputPath, dims,blockUnit );
			outputFile.create();
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void generateN5fromXML(String inputPath, String metadataPath, String outputPath, int id) {
	try {
	System.out.println("Start generating output");
	XMLFile inputFile = XMLFile.XMLFile(inputPath);
	RandomAccessibleInterval<FloatType> virtual = inputFile.fuse();
	String dataset = "/volumes/raw";
	N5Writer writer = new N5FSWriter(outputPath);
	BlocksMetaData md = BlocksMetaData.fromJson(metadataPath);
//	long[] dims = md.getDimensions();
	int blockUnit = md.getBlockUnit();
	int[] blocks = Tools.array(blockUnit, virtual.numDimensions());

	N5Utils.save(virtual, writer, dataset, blocks, new RawCompression());
	System.out.println("Ouptut generated");
} catch (SpimDataException | IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
	}

	public static void main(String[] args) {
//		new ImageJ();
		String str = "-t proc -i /Users/Marwan/Desktop/Task/grid-3d-stitched-h5/dataset.xml -o /Users/Marwan/Desktop/Task/output.n5 -m /Users/Marwan/Desktop/Task/metadata.json -id 1";
//		System.out.println(String.join(" ", args));
		CommandLine.call(new MainJob(), str.split(" "));
	}
}
