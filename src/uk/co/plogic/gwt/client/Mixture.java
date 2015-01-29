package uk.co.plogic.gwt.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.maps.gwt.client.ArrayHelper;

public class Mixture implements EntryPoint {

// see https://code.google.com/p/gwt-google-apis/wiki/VisualizationGettingStarted

	@Override
	public void onModuleLoad() {

	    Runnable onLoadCallback = new Runnable() {
	        public void run() {
	            Panel panel = RootPanel.get();

		        // Create a pie chart visualization.
		        ColumnChart barchart = new ColumnChart(createTable(), createOptions());
		        barchart.addSelectHandler(createSelectHandler(barchart));

		        panel.add(barchart);

		        Selection [] s = {Selection.createCellSelection(2, 1)};
                //Selection [] s = {Selection.createCellSelection(76, 1)};
                JsArray<Selection> selection = ArrayHelper.toJsArray(s);
                //logger.fine("row sel:"+selection.get(0).getRow());


                barchart.setSelections(selection);

		      }
	    };

        // Load the visualization api, passing the onLoadCallback to be called
        // when loading is done.
        VisualizationUtils.loadVisualizationApi(onLoadCallback, ColumnChart.PACKAGE);

	}

	private Options createOptions() {
        Options options = Options.create();
        options.setWidth(600);
        options.setHeight(300);
        //options.set3D(false);
        options.setTitle("Stuff");
        return options;
	}

	private AbstractDataTable createTable() {
	    DataTable data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "Where");
	    data.addColumn(ColumnType.NUMBER, "How much");


        //data.addRows(377);
        data.addRows(3);
        data.setValue(0, 1, 9.580);
	    data.setValue(0, 0, "Wycombe District");
	    data.setValue(1, 1, 99.310);
	    data.setValue(1, 0, "South Bucks District");
	    data.setValue(2, 1, 99.490);
	    data.setValue(2, 0, "Chiltern District");

//	    data.setValue(3, 0, "Aylesbury Vale District"); data.setValue(3, 1, 95.350);
//	    data.setValue(4, 0, "Fenland District"); data.setValue(4, 1, 98.790);
//	    data.setValue(5, 0, "South Cambridgeshire District"); data.setValue(5, 1, 50.830);
//	    data.setValue(6, 0, "East Cambridgeshire District"); data.setValue(6, 1, 99.590);
//	    data.setValue(7, 0, "Huntingdonshire District"); data.setValue(7, 1, 80.210);
//	    data.setValue(8, 0, "Cambridge District (B)"); data.setValue(8, 1, 99.280);
//	    data.setValue(9, 0, "Copeland District (B)"); data.setValue(9, 1, 94.650);
//	    data.setValue(10, 0, "Carlisle District (B)"); data.setValue(10, 1, 92.770);
//	    data.setValue(11, 0, "South Lakeland District"); data.setValue(11, 1, 88.950);
//	    data.setValue(12, 0, "Allerdale District (B)"); data.setValue(12, 1, 98.020);
//	    data.setValue(13, 0, "Eden District"); data.setValue(13, 1, 99.420);
//	    data.setValue(14, 0, "Barrow-in-Furness District (B)"); data.setValue(14, 1, 57.510);
//	    data.setValue(15, 0, "High Peak District (B)"); data.setValue(15, 1, 99.460);
//	    data.setValue(16, 0, "South Derbyshire District"); data.setValue(16, 1, 100.000);
//	    data.setValue(17, 0, "Erewash District (B)"); data.setValue(17, 1, 99.840);
//	    data.setValue(18, 0, "North East Derbyshire District"); data.setValue(18, 1, 97.080);
//	    data.setValue(19, 0, "Amber Valley District (B)"); data.setValue(19, 1, 97.310);
//	    data.setValue(20, 0, "Bolsover District"); data.setValue(20, 1, 67.700);
//	    data.setValue(21, 0, "Derbyshire Dales District"); data.setValue(21, 1, 40.290);
//	    data.setValue(22, 0, "Chesterfield District (B)"); data.setValue(22, 1, 99.780);
//	    data.setValue(23, 0, "North Devon District"); data.setValue(23, 1, 98.730);
//	    data.setValue(24, 0, "East Devon District"); data.setValue(24, 1, 96.070);
//	    data.setValue(25, 0, "Teignbridge District"); data.setValue(25, 1, 95.620);
//	    data.setValue(26, 0, "West Devon District (B)"); data.setValue(26, 1, 72.350);
//	    data.setValue(27, 0, "Mid Devon District"); data.setValue(27, 1, 91.580);
//	    data.setValue(28, 0, "Exeter District (B)"); data.setValue(28, 1, 98.680);
//	    data.setValue(29, 0, "Purbeck District"); data.setValue(29, 1, 84.680);
//	    data.setValue(30, 0, "Christchurch District (B)"); data.setValue(30, 1, 66.000);
//	    data.setValue(31, 0, "West Dorset District"); data.setValue(31, 1, 92.810);
//	    data.setValue(32, 0, "East Dorset District"); data.setValue(32, 1, 80.600);
//	    data.setValue(33, 0, "North Dorset District"); data.setValue(33, 1, 97.540);
//	    data.setValue(34, 0, "Weymouth and Portland District (B)"); data.setValue(34, 1, 98.230);
//	    data.setValue(35, 0, "Lewes District"); data.setValue(35, 1, 100.000);
//	    data.setValue(36, 0, "Rother District"); data.setValue(36, 1, 92.530);
//	    data.setValue(37, 0, "Wealden District"); data.setValue(37, 1, 97.520);
//	    data.setValue(38, 0, "Eastbourne District (B)"); data.setValue(38, 1, 70.270);
//	    data.setValue(39, 0, "Hastings District (B)"); data.setValue(39, 1, 94.250);
//	    data.setValue(40, 0, "Brentwood District (B)"); data.setValue(40, 1, 100.000);
//	    data.setValue(41, 0, "Rochford District"); data.setValue(41, 1, 99.660);
//	    data.setValue(42, 0, "Epping Forest District"); data.setValue(42, 1, 97.790);
//	    data.setValue(43, 0, "Tendring District"); data.setValue(43, 1, 92.290);
//	    data.setValue(44, 0, "Uttlesford District"); data.setValue(44, 1, 94.290);
//	    data.setValue(45, 0, "Chelmsford District (B)"); data.setValue(45, 1, 80.910);
//	    data.setValue(46, 0, "Colchester District (B)"); data.setValue(46, 1, 96.910);
//	    data.setValue(47, 0, "Maldon District (B)"); data.setValue(47, 1, 98.910);
//	    data.setValue(48, 0, "Braintree District"); data.setValue(48, 1, 94.770);
//	    data.setValue(49, 0, "Harlow District"); data.setValue(49, 1, 87.740);
//	    data.setValue(50, 0, "Basildon District (B)"); data.setValue(50, 1, 95.530);
//	    data.setValue(51, 0, "Castle Point District (B)"); data.setValue(51, 1, 98.830);
//	    data.setValue(52, 0, "Forest of Dean District"); data.setValue(52, 1, 83.980);
//	    data.setValue(53, 0, "Cotswold District"); data.setValue(53, 1, 99.250);
//	    data.setValue(54, 0, "Stroud District"); data.setValue(54, 1, 87.560);
//	    data.setValue(55, 0, "Tewkesbury District (B)"); data.setValue(55, 1, 75.090);
//	    data.setValue(56, 0, "Gloucester District (B)"); data.setValue(56, 1, 98.870);
//	    data.setValue(57, 0, "Cheltenham District (B)"); data.setValue(57, 1, 72.570);
//	    data.setValue(58, 0, "Basingstoke and Deane District (B)"); data.setValue(58, 1, 99.480);
//	    data.setValue(59, 0, "New Forest District"); data.setValue(59, 1, 77.970);
//	    data.setValue(60, 0, "Eastleigh District (B)"); data.setValue(60, 1, 96.520);
//	    data.setValue(61, 0, "East Hampshire District"); data.setValue(61, 1, 99.830);
//	    data.setValue(62, 0, "Winchester District (B)"); data.setValue(62, 1, 97.130);
//	    data.setValue(63, 0, "Test Valley District"); data.setValue(63, 1, 99.450);
//	    data.setValue(64, 0, "Hart District"); data.setValue(64, 1, 91.980);
//	    data.setValue(65, 0, "Gosport District (B)"); data.setValue(65, 1, 95.990);
//	    data.setValue(66, 0, "Fareham District (B)"); data.setValue(66, 1, 97.720);
//	    data.setValue(67, 0, "Havant District (B)"); data.setValue(67, 1, 97.070);
//	    data.setValue(68, 0, "Rushmoor District (B)"); data.setValue(68, 1, 97.630);
//	    data.setValue(69, 0, "Three Rivers District"); data.setValue(69, 1, 97.290);
//	    data.setValue(70, 0, "Hertsmere District (B)"); data.setValue(70, 1, 95.450);
//	    data.setValue(71, 0, "Broxbourne District (B)"); data.setValue(71, 1, 100.000);
//	    data.setValue(72, 0, "Dacorum District (B)"); data.setValue(72, 1, 96.060);
//	    data.setValue(73, 0, "East Hertfordshire District"); data.setValue(73, 1, 48.100);
//	    data.setValue(74, 0, "St. Albans District (B)"); data.setValue(74, 1, 100.000);
//	    data.setValue(75, 0, "Welwyn Hatfield District (B)"); data.setValue(75, 1, 52.480);
//	    data.setValue(76, 0, "North Hertfordshire District"); data.setValue(76, 1, 83.500);
//	    data.setValue(77, 0, "Watford District (B)"); data.setValue(77, 1, 93.670);
//	    data.setValue(78, 0, "Stevenage District (B)"); data.setValue(78, 1, 98.860);
//	    data.setValue(79, 0, "Tunbridge Wells District (B)"); data.setValue(79, 1, 92.640);
//	    data.setValue(80, 0, "Shepway District"); data.setValue(80, 1, 94.710);
//	    data.setValue(81, 0, "Sevenoaks District"); data.setValue(81, 1, 95.440);
//	    data.setValue(82, 0, "Tonbridge and Malling District (B)"); data.setValue(82, 1, 99.270);
//	    data.setValue(83, 0, "Thanet District"); data.setValue(83, 1, 96.550);
//	    data.setValue(84, 0, "Ashford District (B)"); data.setValue(84, 1, 99.560);
//	    data.setValue(85, 0, "Canterbury District (B)"); data.setValue(85, 1, 99.330);
//	    data.setValue(86, 0, "Dover District"); data.setValue(86, 1, 97.580);
//	    data.setValue(87, 0, "Maidstone District (B)"); data.setValue(87, 1, 90.540);
//	    data.setValue(88, 0, "Swale District (B)"); data.setValue(88, 1, 91.740);
//	    data.setValue(89, 0, "Dartford District (B)"); data.setValue(89, 1, 92.940);
//	    data.setValue(90, 0, "Gravesham District (B)"); data.setValue(90, 1, 100.000);
//	    data.setValue(91, 0, "West Lancashire District (B)"); data.setValue(91, 1, 87.700);
//	    data.setValue(92, 0, "Lancaster District (B)"); data.setValue(92, 1, 78.790);
//	    data.setValue(93, 0, "Chorley District (B)"); data.setValue(93, 1, 88.700);
//	    data.setValue(94, 0, "South Ribble District (B)"); data.setValue(94, 1, 90.090);
//	    data.setValue(95, 0, "Rossendale District (B)"); data.setValue(95, 1, 100.000);
//	    data.setValue(96, 0, "Fylde District (B)"); data.setValue(96, 1, 88.070);
//	    data.setValue(97, 0, "Preston District (B)"); data.setValue(97, 1, 100.000);
//	    data.setValue(98, 0, "Wyre District (B)"); data.setValue(98, 1, 95.280);
//	    data.setValue(99, 0, "Pendle District (B)"); data.setValue(99, 1, 74.270);
//	    data.setValue(100, 0, "Ribble Valley District (B)"); data.setValue(100, 1, 96.800);
//	    data.setValue(101, 0, "Hyndburn District (B)"); data.setValue(101, 1, 73.280);
//	    data.setValue(102, 0, "Burnley District (B)"); data.setValue(102, 1, 99.810);
//	    data.setValue(103, 0, "Hinckley and Bosworth District (B)"); data.setValue(103, 1, 83.670);
//	    data.setValue(104, 0, "North West Leicestershire District"); data.setValue(104, 1, 92.860);
//	    data.setValue(105, 0, "Melton District (B)"); data.setValue(105, 1, 98.700);
//	    data.setValue(106, 0, "Harborough District"); data.setValue(106, 1, 82.350);
//	    data.setValue(107, 0, "Blaby District"); data.setValue(107, 1, 82.620);
//	    data.setValue(108, 0, "Charnwood District (B)"); data.setValue(108, 1, 99.250);
//	    data.setValue(109, 0, "Oadby and Wigston District (B)"); data.setValue(109, 1, 100.000);
//	    data.setValue(110, 0, "West Lindsey District"); data.setValue(110, 1, 67.420);
//	    data.setValue(111, 0, "South Kesteven District"); data.setValue(111, 1, 98.170);
//	    data.setValue(112, 0, "South Holland District"); data.setValue(112, 1, 93.150);
//	    data.setValue(113, 0, "Boston District (B)"); data.setValue(113, 1, 80.150);
//	    data.setValue(114, 0, "North Kesteven District"); data.setValue(114, 1, 97.570);
//	    data.setValue(115, 0, "East Lindsey District"); data.setValue(115, 1, 72.310);
//	    data.setValue(116, 0, "Lincoln District (B)"); data.setValue(116, 1, 90.620);
//	    data.setValue(117, 0, "North Norfolk District"); data.setValue(117, 1, 67.620);
//	    data.setValue(118, 0, "Great Yarmouth District (B)"); data.setValue(118, 1, 37.140);
//	    data.setValue(119, 0, "King's Lynn and West Norfolk District (B)"); data.setValue(119, 1, 67.120);
//	    data.setValue(120, 0, "Breckland District"); data.setValue(120, 1, 99.720);
//	    data.setValue(121, 0, "Norwich District (B)"); data.setValue(121, 1, 90.190);
//	    data.setValue(122, 0, "South Northamptonshire District"); data.setValue(122, 1, 95.310);
//	    data.setValue(123, 0, "East Northamptonshire District"); data.setValue(123, 1, 80.000);
//	    data.setValue(124, 0, "Daventry District"); data.setValue(124, 1, 79.500);
//	    data.setValue(125, 0, "Wellingborough District (B)"); data.setValue(125, 1, 93.850);
//	    data.setValue(126, 0, "Kettering District (B)"); data.setValue(126, 1, 69.760);
//	    data.setValue(127, 0, "Northampton District (B)"); data.setValue(127, 1, 67.980);
//	    data.setValue(128, 0, "Corby District (B)"); data.setValue(128, 1, 83.680);
//	    data.setValue(129, 0, "Scarborough District (B)"); data.setValue(129, 1, 92.850);
//	    data.setValue(130, 0, "Selby District"); data.setValue(130, 1, 95.260);
//	    data.setValue(131, 0, "Craven District"); data.setValue(131, 1, 66.500);
//	    data.setValue(132, 0, "Richmondshire District"); data.setValue(132, 1, 85.410);
//	    data.setValue(133, 0, "Harrogate District (B)"); data.setValue(133, 1, 77.130);
//	    data.setValue(134, 0, "Ryedale District"); data.setValue(134, 1, 98.230);
//	    data.setValue(135, 0, "Hambleton District"); data.setValue(135, 1, 93.620);
//	    data.setValue(136, 0, "Bassetlaw District"); data.setValue(136, 1, 99.430);
//	    data.setValue(137, 0, "Rushcliffe District (B)"); data.setValue(137, 1, 93.670);
//	    data.setValue(138, 0, "Gedling District (B)"); data.setValue(138, 1, 98.630);
//	    data.setValue(139, 0, "Ashfield District"); data.setValue(139, 1, 100.000);
//	    data.setValue(140, 0, "Newark and Sherwood District"); data.setValue(140, 1, 87.110);
//	    data.setValue(141, 0, "Broxtowe District (B)"); data.setValue(141, 1, 77.590);
//	    data.setValue(142, 0, "Mansfield District"); data.setValue(142, 1, 56.530);
//	    data.setValue(143, 0, "Vale of White Horse District"); data.setValue(143, 1, 100.000);
//	    data.setValue(144, 0, "South Oxfordshire District"); data.setValue(144, 1, 100.000);
//	    data.setValue(145, 0, "Cherwell District"); data.setValue(145, 1, 100.000);
//	    data.setValue(146, 0, "West Oxfordshire District"); data.setValue(146, 1, 98.760);
//	    data.setValue(147, 0, "Oxford District (B)"); data.setValue(147, 1, 92.770);
//	    data.setValue(148, 0, "West Somerset District"); data.setValue(148, 1, 90.370);
//	    data.setValue(149, 0, "Mendip District"); data.setValue(149, 1, 79.700);
//	    data.setValue(150, 0, "Taunton Deane District (B)"); data.setValue(150, 1, 78.300);
//	    data.setValue(151, 0, "South Somerset District"); data.setValue(151, 1, 100.000);
//	    data.setValue(152, 0, "Sedgemoor District"); data.setValue(152, 1, 100.000);
//	    data.setValue(153, 0, "Staffordshire Moorlands District"); data.setValue(153, 1, 99.810);
//	    data.setValue(154, 0, "South Staffordshire District"); data.setValue(154, 1, 99.830);
//	    data.setValue(155, 0, "Lichfield District"); data.setValue(155, 1, 99.290);
//	    data.setValue(156, 0, "Newcastle-under-Lyme District (B)"); data.setValue(156, 1, 79.910);
//	    data.setValue(157, 0, "Stafford District (B)"); data.setValue(157, 1, 95.710);
//	    data.setValue(158, 0, "East Staffordshire District (B)"); data.setValue(158, 1, 89.280);
//	    data.setValue(159, 0, "Cannock Chase District"); data.setValue(159, 1, 71.270);
//	    data.setValue(160, 0, "Tamworth District (B)"); data.setValue(160, 1, 100.000);
//	    data.setValue(161, 0, "Waveney District"); data.setValue(161, 1, 96.480);
//	    data.setValue(162, 0, "Babergh District"); data.setValue(162, 1, 80.460);
//	    data.setValue(163, 0, "Suffolk Coastal District"); data.setValue(163, 1, 65.690);
//	    data.setValue(164, 0, "St. Edmundsbury District (B)"); data.setValue(164, 1, 82.450);
//	    data.setValue(165, 0, "Forest Heath District"); data.setValue(165, 1, 89.740);
//	    data.setValue(166, 0, "Mid Suffolk District"); data.setValue(166, 1, 80.460);
//	    data.setValue(167, 0, "Ipswich District (B)"); data.setValue(167, 1, 75.730);
//	    data.setValue(168, 0, "Waverley District (B)"); data.setValue(168, 1, 98.770);
//	    data.setValue(169, 0, "Tandridge District"); data.setValue(169, 1, 90.410);
//	    data.setValue(170, 0, "Woking District (B)"); data.setValue(170, 1, 99.750);
//	    data.setValue(171, 0, "Surrey Heath District (B)"); data.setValue(171, 1, 100.000);
//	    data.setValue(172, 0, "Runnymede District (B)"); data.setValue(172, 1, 90.800);
//	    data.setValue(173, 0, "Guildford District (B)"); data.setValue(173, 1, 58.160);
//	    data.setValue(174, 0, "Reigate and Banstead District (B)"); data.setValue(174, 1, 100.000);
//	    data.setValue(175, 0, "Mole Valley District"); data.setValue(175, 1, 99.470);
//	    data.setValue(176, 0, "Elmbridge District (B)"); data.setValue(176, 1, 94.660);
//	    data.setValue(177, 0, "Spelthorne District (B)"); data.setValue(177, 1, 84.420);
//	    data.setValue(178, 0, "Epsom and Ewell District (B)"); data.setValue(178, 1, 92.810);
//	    data.setValue(179, 0, "North Warwickshire District (B)"); data.setValue(179, 1, 100.000);
//	    data.setValue(180, 0, "Rugby District (B)"); data.setValue(180, 1, 91.620);
//	    data.setValue(181, 0, "Warwick District"); data.setValue(181, 1, 93.020);
//	    data.setValue(182, 0, "Stratford-on-Avon District"); data.setValue(182, 1, 96.530);
//	    data.setValue(183, 0, "Nuneaton and Bedworth District (B)"); data.setValue(183, 1, 95.700);
//	    data.setValue(184, 0, "Arun District"); data.setValue(184, 1, 89.880);
//	    data.setValue(185, 0, "Adur District"); data.setValue(185, 1, 82.310);
//	    data.setValue(186, 0, "Chichester District"); data.setValue(186, 1, 97.450);
//	    data.setValue(187, 0, "Mid Sussex District"); data.setValue(187, 1, 97.130);
//	    data.setValue(188, 0, "Horsham District"); data.setValue(188, 1, 96.330);
//	    data.setValue(189, 0, "Worthing District (B)"); data.setValue(189, 1, 82.310);
//	    data.setValue(190, 0, "Crawley District (B)"); data.setValue(190, 1, 78.000);
//	    data.setValue(191, 0, "Malvern Hills District"); data.setValue(191, 1, 69.500);
//	    data.setValue(192, 0, "Wyre Forest District"); data.setValue(192, 1, 89.790);
//	    data.setValue(193, 0, "Wychavon District"); data.setValue(193, 1, 64.860);
//	    data.setValue(194, 0, "Bromsgrove District"); data.setValue(194, 1, 56.940);
//	    data.setValue(195, 0, "Worcester District (B)"); data.setValue(195, 1, 80.380);
//	    data.setValue(196, 0, "Redditch District (B)"); data.setValue(196, 1, 79.380);
//	    data.setValue(197, 0, "Kingston upon Thames London Boro"); data.setValue(197, 1, 52.200);
//	    data.setValue(198, 0, "Croydon London Boro"); data.setValue(198, 1, 72.460);
//	    data.setValue(199, 0, "Bromley London Boro"); data.setValue(199, 1, 86.340);
//	    data.setValue(200, 0, "Hounslow London Boro"); data.setValue(200, 1, 98.070);
//	    data.setValue(201, 0, "Ealing London Boro"); data.setValue(201, 1, 57.860);
//	    data.setValue(202, 0, "Havering London Boro"); data.setValue(202, 1, 40.480);
//	    data.setValue(203, 0, "Hillingdon London Boro"); data.setValue(203, 1, 88.910);
//	    data.setValue(204, 0, "Harrow London Boro"); data.setValue(204, 1, 57.400);
//	    data.setValue(205, 0, "Brent London Boro"); data.setValue(205, 1, 19.080);
//	    data.setValue(206, 0, "Barnet London Boro"); data.setValue(206, 1, 96.890);
//	    data.setValue(207, 0, "Lambeth London Boro"); data.setValue(207, 1, 67.540);
//	    data.setValue(208, 0, "Southwark London Boro"); data.setValue(208, 1, 56.350);
//	    data.setValue(209, 0, "Lewisham London Boro"); data.setValue(209, 1, 52.260);
//	    data.setValue(210, 0, "Greenwich London Boro"); data.setValue(210, 1, 75.890);
//	    data.setValue(211, 0, "Bexley London Boro"); data.setValue(211, 1, 91.410);
//	    data.setValue(212, 0, "Enfield London Boro"); data.setValue(212, 1, 79.230);
//	    data.setValue(213, 0, "Waltham Forest London Boro"); data.setValue(213, 1, 91.090);
//	    data.setValue(214, 0, "Redbridge London Boro"); data.setValue(214, 1, 69.370);
//	    data.setValue(215, 0, "Sutton London Boro"); data.setValue(215, 1, 88.980);
//	    data.setValue(216, 0, "Richmond upon Thames London Boro"); data.setValue(216, 1, 65.860);
//	    data.setValue(217, 0, "Merton London Boro"); data.setValue(217, 1, 70.800);
//	    data.setValue(218, 0, "Wandsworth London Boro"); data.setValue(218, 1, 84.000);
//	    data.setValue(219, 0, "Hammersmith and Fulham London Boro"); data.setValue(219, 1, 86.580);
//	    data.setValue(220, 0, "Kensington and Chelsea London Boro"); data.setValue(220, 1, 93.300);
//	    data.setValue(221, 0, "City of Westminster London Boro"); data.setValue(221, 1, 71.190);
//	    data.setValue(222, 0, "Camden London Boro"); data.setValue(222, 1, 44.750);
//	    data.setValue(223, 0, "Tower Hamlets London Boro"); data.setValue(223, 1, 80.340);
//	    data.setValue(224, 0, "Islington London Boro"); data.setValue(224, 1, 99.650);
//	    data.setValue(225, 0, "Hackney London Boro"); data.setValue(225, 1, 91.520);
//	    data.setValue(226, 0, "Haringey London Boro"); data.setValue(226, 1, 81.540);
//	    data.setValue(227, 0, "Newham London Boro"); data.setValue(227, 1, 64.450);
//	    data.setValue(228, 0, "Barking and Dagenham London Boro"); data.setValue(228, 1, 81.460);
//	    data.setValue(229, 0, "City and County of the City of London"); data.setValue(229, 1, 98.030);
//	    data.setValue(230, 0, "Barnsley District (B)"); data.setValue(230, 1, 65.330);
//	    data.setValue(231, 0, "Birmingham District (B)"); data.setValue(231, 1, 71.120);
//	    data.setValue(232, 0, "Bolton District (B)"); data.setValue(232, 1, 74.940);
//	    data.setValue(233, 0, "Bradford District (B)"); data.setValue(233, 1, 80.670);
//	    data.setValue(234, 0, "Bury District (B)"); data.setValue(234, 1, 30.020);
//	    data.setValue(235, 0, "Calderdale District (B)"); data.setValue(235, 1, 98.550);
//	    data.setValue(236, 0, "City of Wolverhampton District (B)"); data.setValue(236, 1, 73.180);
//	    data.setValue(237, 0, "Coventry District (B)"); data.setValue(237, 1, 82.760);
//	    data.setValue(238, 0, "Doncaster District (B)"); data.setValue(238, 1, 59.430);
//	    data.setValue(239, 0, "Dudley District (B)"); data.setValue(239, 1, 84.380);
//	    data.setValue(240, 0, "Gateshead District (B)"); data.setValue(240, 1, 92.430);
//	    data.setValue(241, 0, "Kirklees District (B)"); data.setValue(241, 1, 75.670);
//	    data.setValue(242, 0, "Knowsley District (B)"); data.setValue(242, 1, 83.030);
//	    data.setValue(243, 0, "Leeds District (B)"); data.setValue(243, 1, 94.580);
//	    data.setValue(244, 0, "Liverpool District (B)"); data.setValue(244, 1, 77.630);
//	    data.setValue(245, 0, "Manchester District (B)"); data.setValue(245, 1, 92.790);
//	    data.setValue(246, 0, "Newcastle upon Tyne District (B)"); data.setValue(246, 1, 89.670);
//	    data.setValue(247, 0, "North Tyneside District (B)"); data.setValue(247, 1, 82.230);
//	    data.setValue(248, 0, "Oldham District (B)"); data.setValue(248, 1, 56.870);
//	    data.setValue(249, 0, "Rochdale District (B)"); data.setValue(249, 1, 13.280);
//	    data.setValue(250, 0, "Rotherham District (B)"); data.setValue(250, 1, 91.900);
//	    data.setValue(251, 0, "Salford District (B)"); data.setValue(251, 1, 98.240);
//	    data.setValue(252, 0, "Sandwell District (B)"); data.setValue(252, 1, 100.000);
//	    data.setValue(253, 0, "Sefton District (B)"); data.setValue(253, 1, 98.430);
//	    data.setValue(254, 0, "Sheffield District (B)"); data.setValue(254, 1, 70.580);
//	    data.setValue(255, 0, "Solihull District (B)"); data.setValue(255, 1, 100.000);
//	    data.setValue(256, 0, "South Tyneside District (B)"); data.setValue(256, 1, 79.370);
//	    data.setValue(257, 0, "Stockport District (B)"); data.setValue(257, 1, 52.540);
//	    data.setValue(258, 0, "St. Helens District (B)"); data.setValue(258, 1, 92.150);
//	    data.setValue(259, 0, "Sunderland District (B)"); data.setValue(259, 1, 87.380);
//	    data.setValue(260, 0, "Tameside District (B)"); data.setValue(260, 1, 82.780);
//	    data.setValue(261, 0, "Trafford District (B)"); data.setValue(261, 1, 76.660);
//	    data.setValue(262, 0, "Wakefield District (B)"); data.setValue(262, 1, 75.160);
//	    data.setValue(263, 0, "Walsall District (B)"); data.setValue(263, 1, 96.310);
//	    data.setValue(264, 0, "Wigan District (B)"); data.setValue(264, 1, 81.200);
//	    data.setValue(265, 0, "Wirral District (B)"); data.setValue(265, 1, 75.830);
//	    data.setValue(266, 0, "Abertawe - Swansea"); data.setValue(266, 1, 88.050);
//	    data.setValue(267, 0, "Angus"); data.setValue(267, 1, 86.190);
//	    data.setValue(268, 0, "Bath and North East Somerset"); data.setValue(268, 1, 57.630);
//	    data.setValue(269, 0, "Bedford (B)"); data.setValue(269, 1, 94.610);
//	    data.setValue(270, 0, "Blackburn with Darwen (B)"); data.setValue(270, 1, 89.250);
//	    data.setValue(271, 0, "Blackpool (B)"); data.setValue(271, 1, 46.120);
//	    data.setValue(272, 0, "Blaenau Gwent - Blaenau Gwent"); data.setValue(272, 1, 97.390);
//	    data.setValue(273, 0, "Bournemouth (B)"); data.setValue(273, 1, 74.170);
//	    data.setValue(274, 0, "Bracknell Forest (B)"); data.setValue(274, 1, 94.880);
//	    data.setValue(275, 0, "Bro Morgannwg - the Vale of Glamorgan"); data.setValue(275, 1, 100.000);
//	    data.setValue(276, 0, "Caerffili - Caerphilly"); data.setValue(276, 1, 82.050);
//	    data.setValue(277, 0, "Casnewydd - Newport"); data.setValue(277, 1, 95.060);
//	    data.setValue(278, 0, "Central Bedfordshire"); data.setValue(278, 1, 62.600);
//	    data.setValue(279, 0, "Cheshire East (B)"); data.setValue(279, 1, 63.550);
//	    data.setValue(280, 0, "Cheshire West and Chester (B)"); data.setValue(280, 1, 91.210);
//	    data.setValue(281, 0, "City of Bristol (B)"); data.setValue(281, 1, 41.840);
//	    data.setValue(282, 0, "City of Derby (B)"); data.setValue(282, 1, 69.990);
//	    data.setValue(283, 0, "City of Kingston upon Hull (B)"); data.setValue(283, 1, 83.110);
//	    data.setValue(284, 0, "City of Leicester (B)"); data.setValue(284, 1, 90.870);
//	    data.setValue(285, 0, "City of Nottingham (B)"); data.setValue(285, 1, 70.150);
//	    data.setValue(286, 0, "City of Peterborough (B)"); data.setValue(286, 1, 76.030);
//	    data.setValue(287, 0, "City of Southampton (B)"); data.setValue(287, 1, 82.000);
//	    data.setValue(288, 0, "City of Stoke-on-Trent (B)"); data.setValue(288, 1, 52.600);
//	    data.setValue(289, 0, "Clackmannanshire"); data.setValue(289, 1, 100.000);
//	    data.setValue(290, 0, "Conwy - Conwy"); data.setValue(290, 1, 95.230);
//	    data.setValue(291, 0, "County Durham"); data.setValue(291, 1, 70.290);
//	    data.setValue(292, 0, "County of Herefordshire"); data.setValue(292, 1, 88.930);
//	    data.setValue(293, 0, "Darlington (B)"); data.setValue(293, 1, 98.510);
//	    data.setValue(294, 0, "Dundee City"); data.setValue(294, 1, 76.990);
//	    data.setValue(295, 0, "East Ayrshire"); data.setValue(295, 1, 99.800);
//	    data.setValue(296, 0, "East Dunbartonshire"); data.setValue(296, 1, 96.280);
//	    data.setValue(297, 0, "East Renfrewshire"); data.setValue(297, 1, 100.000);
//	    data.setValue(298, 0, "East Riding of Yorkshire"); data.setValue(298, 1, 90.180);
//	    data.setValue(299, 0, "Falkirk"); data.setValue(299, 1, 97.200);
//	    data.setValue(300, 0, "Glasgow City"); data.setValue(300, 1, 83.930);
//	    data.setValue(301, 0, "Halton (B)"); data.setValue(301, 1, 99.830);
//	    data.setValue(302, 0, "Hartlepool (B)"); data.setValue(302, 1, 100.000);
//	    data.setValue(303, 0, "Inverclyde"); data.setValue(303, 1, 82.040);
//	    data.setValue(304, 0, "Luton (B)"); data.setValue(304, 1, 61.730);
//	    data.setValue(305, 0, "Medway (B)"); data.setValue(305, 1, 48.070);
//	    data.setValue(306, 0, "Merthyr Tudful - Merthyr Tydfil"); data.setValue(306, 1, 91.390);
//	    data.setValue(307, 0, "Middlesbrough (B)"); data.setValue(307, 1, 74.080);
//	    data.setValue(308, 0, "Midlothian"); data.setValue(308, 1, 94.580);
//	    data.setValue(309, 0, "Milton Keynes (B)"); data.setValue(309, 1, 40.140);
//	    data.setValue(310, 0, "North East Lincolnshire (B)"); data.setValue(310, 1, 65.680);
//	    data.setValue(311, 0, "North Lanarkshire"); data.setValue(311, 1, 100.000);
//	    data.setValue(312, 0, "North Lincolnshire (B)"); data.setValue(312, 1, 99.300);
//	    data.setValue(313, 0, "Pen-y-bont ar Ogwr - Bridgend"); data.setValue(313, 1, 91.470);
//	    data.setValue(314, 0, "Perth and Kinross"); data.setValue(314, 1, 86.510);
//	    data.setValue(315, 0, "Poole (B)"); data.setValue(315, 1, 49.290);
//	    data.setValue(316, 0, "Powys - Powys"); data.setValue(316, 1, 100.000);
//	    data.setValue(317, 0, "Reading (B)"); data.setValue(317, 1, 84.360);
//	    data.setValue(318, 0, "Redcar and Cleveland (B)"); data.setValue(318, 1, 64.490);
//	    data.setValue(319, 0, "Renfrewshire"); data.setValue(319, 1, 98.800);
//	    data.setValue(320, 0, "Rhondda Cynon Taf - Rhondda Cynon Taf"); data.setValue(320, 1, 100.000);
//	    data.setValue(321, 0, "Rutland"); data.setValue(321, 1, 95.340);
//	    data.setValue(322, 0, "Scottish Borders"); data.setValue(322, 1, 75.620);
//	    data.setValue(323, 0, "Shropshire"); data.setValue(323, 1, 50.020);
//	    data.setValue(324, 0, "Sir Ddinbych - Denbighshire"); data.setValue(324, 1, 93.680);
//	    data.setValue(325, 0, "Sir Gaerfyrddin - Carmarthenshire"); data.setValue(325, 1, 100.000);
//	    data.setValue(326, 0, "Sir y Fflint - Flintshire"); data.setValue(326, 1, 84.750);
//	    data.setValue(327, 0, "Slough (B)"); data.setValue(327, 1, 40.570);
//	    data.setValue(328, 0, "Southend-on-Sea (B)"); data.setValue(328, 1, 69.040);
//	    data.setValue(329, 0, "South Gloucestershire"); data.setValue(329, 1, 70.550);
//	    data.setValue(330, 0, "South Lanarkshire"); data.setValue(330, 1, 98.760);
//	    data.setValue(331, 0, "Stirling"); data.setValue(331, 1, 94.180);
//	    data.setValue(332, 0, "Stockton-on-Tees (B)"); data.setValue(332, 1, 98.530);
//	    data.setValue(333, 0, "Swindon (B)"); data.setValue(333, 1, 91.220);
//	    data.setValue(334, 0, "Telford and Wrekin (B)"); data.setValue(334, 1, 68.180);
//	    data.setValue(335, 0, "The City of Brighton and Hove (B)"); data.setValue(335, 1, 99.950);
//	    data.setValue(336, 0, "Thurrock (B)"); data.setValue(336, 1, 86.160);
//	    data.setValue(337, 0, "Tor-faen - Torfaen"); data.setValue(337, 1, 100.000);
//	    data.setValue(338, 0, "Warrington (B)"); data.setValue(338, 1, 58.360);
//	    data.setValue(339, 0, "West Berkshire"); data.setValue(339, 1, 98.110);
//	    data.setValue(340, 0, "West Dunbartonshire"); data.setValue(340, 1, 96.330);
//	    data.setValue(341, 0, "West Lothian"); data.setValue(341, 1, 98.470);
//	    data.setValue(342, 0, "Wiltshire"); data.setValue(342, 1, 38.990);
//	    data.setValue(343, 0, "Windsor and Maidenhead (B)"); data.setValue(343, 1, 76.150);
//	    data.setValue(344, 0, "Wokingham (B)"); data.setValue(344, 1, 98.200);
//	    data.setValue(345, 0, "Wrecsam - Wrexham"); data.setValue(345, 1, 77.120);
//	    data.setValue(346, 0, "York (B)"); data.setValue(346, 1, 56.090);
//	    data.setValue(347, 0, "Broadland District"); data.setValue(347, 1, 65.410);
//	    data.setValue(348, 0, "South Hams District"); data.setValue(348, 1, 53.770);
//	    data.setValue(349, 0, "Torridge District"); data.setValue(349, 1, 98.160);
//	    data.setValue(350, 0, "Isle of Wight"); data.setValue(350, 1, 77.850);
//	    data.setValue(351, 0, "Sir Ynys Mon - Isle of Anglesey"); data.setValue(351, 1, 92.070);
//	    data.setValue(352, 0, "Gwynedd - Gwynedd"); data.setValue(352, 1, 99.560);
//	    data.setValue(353, 0, "Caerdydd - Cardiff"); data.setValue(353, 1, 84.920);
//	    data.setValue(354, 0, "Sir Ceredigion - Ceredigion"); data.setValue(354, 1, 89.040);
//	    data.setValue(355, 0, "Sir Fynwy - Monmouthshire"); data.setValue(355, 1, 99.870);
//	    data.setValue(356, 0, "Sir Benfro - Pembrokeshire"); data.setValue(356, 1, 91.070);
//	    data.setValue(357, 0, "North Somerset"); data.setValue(357, 1, 30.480);
//	    data.setValue(358, 0, "Highland"); data.setValue(358, 1, 58.020);
//	    data.setValue(359, 0, "Moray"); data.setValue(359, 1, 86.640);
//	    data.setValue(360, 0, "Orkney Islands"); data.setValue(360, 1, 98.760);
//	    data.setValue(361, 0, "Na h-Eileanan an Iar"); data.setValue(361, 1, 72.800);
//	    data.setValue(362, 0, "Argyll and Bute"); data.setValue(362, 1, 59.870);
//	    data.setValue(363, 0, "Aberdeenshire"); data.setValue(363, 1, 60.910);
//	    data.setValue(364, 0, "Fife"); data.setValue(364, 1, 85.560);
//	    data.setValue(365, 0, "Aberdeen City"); data.setValue(365, 1, 69.010);
//	    data.setValue(366, 0, "City of Edinburgh"); data.setValue(366, 1, 55.290);
//	    data.setValue(367, 0, "East Lothian"); data.setValue(367, 1, 94.750);
//	    data.setValue(368, 0, "Shetland Islands"); data.setValue(368, 1, 92.660);
//	    data.setValue(369, 0, "North Ayrshire"); data.setValue(369, 1, 96.950);
//	    data.setValue(370, 0, "Dumfries and Galloway"); data.setValue(370, 1, 74.740);
//	    data.setValue(371, 0, "City of Portsmouth (B)"); data.setValue(371, 1, 82.460);
//	    data.setValue(372, 0, "City of Plymouth (B)"); data.setValue(372, 1, 83.970);
//	    data.setValue(373, 0, "South Ayrshire"); data.setValue(373, 1, 67.780);
//	    data.setValue(374, 0, "Northumberland"); data.setValue(374, 1, 89.050);
//	    data.setValue(375, 0, "Cornwall"); data.setValue(375, 1, 81.250);
//	    data.setValue(376, 0, "Isles of Scilly"); data.setValue(376, 1, 75.000);

	    return data;
	}
      private SelectHandler createSelectHandler(final ColumnChart chart) {
      return new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          String message = "";

          // May be multiple selections.
          JsArray<Selection> selections = chart.getSelections();

          for (int i = 0; i < selections.length(); i++) {
            // add a new line for each selection
            message += i == 0 ? "" : "\n";

            Selection selection = selections.get(i);

            if (selection.isCell()) {
              // isCell() returns true if a cell has been selected.

              // getRow() returns the row number of the selected cell.
              int row = selection.getRow();
              // getColumn() returns the column number of the selected cell.
              int column = selection.getColumn();
              message += "cell " + row + ":" + column + " selected";
            } else if (selection.isRow()) {
              // isRow() returns true if an entire row has been selected.

              // getRow() returns the row number of the selected row.
              int row = selection.getRow();
              message += "row " + row + " selected";
            } else {
              // unreachable
              message += "Pie chart selections should be either row selections or cell selections.";
              message += "  Other visualizations support column selections as well.";
            }
          }

          Window.alert(message);
        }
      };
    }
}
