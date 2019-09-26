package net.preibisch.fusiontask.task;

public class Fusion {

//	public class FusionParameters 
//	{
//		// imput: spimdata (xml)
//		// which views (list of viewid, which is two integers - timepointid, setupid)
//		// bounding box (which part to process)
//		// downsampling
//		// ...
//		
//	}
//
//
//	public static void main( String[] args ) throws SpimDataException
//	{
//		// imput: spimdata (xml)
//		// which views (list of viewid, which is two integers - timepointid, setupid)
//		// bounding box (which part to process)
//		// downsampling
//		// ...
//		
//		List< Pair<Object, Class >> parameters;
//
//		// generate 4 views with 1000 corresponding beads, single timepoint
//		SpimData2 spimData = SpimData2.convert( SimulatedBeadsImgLoader.spimdataExample( new int[]{ 0, 90, 135 } ) );
//
//		// load drosophila
//		//spimData = new XmlIoSpimData2( "" ).load( "/Users/spreibi/Documents/Microscopy/SPIM/HisYFP-SPIM/dataset.xml" );
//		
//		// select views to process
//		final List< ViewId > viewIds = new ArrayList< ViewId >();
//		viewIds.addAll( spimData.getSequenceDescription().getViewDescriptions().values() );
//
//		// small part of the bounding box\
//		Interval bb = new FinalInterval( new long[] {0, 0, 0 }, new long[] {100, 250, 350} );
//
//		// downsampling
//		double downsampling = Double.NaN; //2.0
//
//		// perforn the fusion virtually
//		RandomAccessibleInterval< FloatType > virtual = FusionTools.fuseVirtual( spimData, viewIds, bb, downsampling );
//
//		// make a physical copy of the virtual randomaccessibleinterval
//		//final RandomAccessibleInterval< FloatType > fusedImg = FusionTools.copyImg( virtual, new ImagePlusImgFactory<>( new FloatType() ), new FloatType(), null, true );
//		
//		// save as one of the blocks of the N5
//		new ImageJ();
//		ImageJFunctions.show( virtual );
//
//		
//		// task #2
//		// small part of the bounding box\
//		bb = new FinalInterval( new long[] {101, 0, 0 }, new long[] {200, 250, 350} );
//
//		// perforn the fusion virtually
//		final RandomAccessibleInterval< FloatType > virtual2 = FusionTools.fuseVirtual( spimData, viewIds, bb, downsampling );
//		ImageJFunctions.show( virtual2 );
//	}
//	
//	
//	public static RandomAccessibleInterval<FloatType> Fusion(String xml, long[] x1, long[] x2) throws SpimDataException{
//		System.out.println(xml);
//		SpimData2 spimData = new XmlIoSpimData2( "" ).load(xml);
//		Interval bb = new FinalInterval(x1, x2);
//
//		final List< ViewId > viewIds = new ArrayList< ViewId >();
//		viewIds.addAll( spimData.getSequenceDescription().getViewDescriptions().values() );
//		
//		double downsampling = Double.NaN; 
//
//		final RandomAccessibleInterval< FloatType > virtual = FusionTools.fuseVirtual( spimData, viewIds, bb, downsampling );
//		
//		return virtual;
//	}
}
