package dpdetector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import recoder.ParserException;
import dpdetector.DetectorManager;
import dpdetector.DetectorManager.DetectorEnum;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GUIMain extends javax.swing.JFrame implements ActionListener {
	private JTextField srcPrjTF;
	private JTextField outputDirTF;
	private JCheckBox adapterCB;
	private JButton outputActionBtn;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JTextArea statTA;
	private JTextArea consoleTA;
	private JCheckBox flyweightCB;
	private JCheckBox facadeCB;
	private JCheckBox proxyCB;
	private JCheckBox bridgeCB;
	private JButton proceedBtn;
	private JCheckBox decoratorCB;
	private JPanel jPanel1;
	private JCheckBox compositeCB;
	private JButton seOutPutDirBtn;
	private JButton sePrjBtn;
	private JFileChooser prjFileChooser;
	private JFileChooser outpufPathChooser;
	
	private DetectorManager detector;
	
	

	


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIMain inst = new GUIMain();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public GUIMain() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
				{
					srcPrjTF = new JTextField();
					getContentPane().add(srcPrjTF);
					srcPrjTF.setBounds(48, 23, 430, 24);
				}
				{
					outputDirTF = new JTextField();
					getContentPane().add(outputDirTF);
					outputDirTF.setBounds(43, 489, 430, 24);
				}
				{
					sePrjBtn = new JButton();
					getContentPane().add(sePrjBtn);
					sePrjBtn.setText("Choose Source Project File");
					sePrjBtn.setBounds(500, 23, 195, 24);
					sePrjBtn.setActionCommand("ChoosePrjFile");
					sePrjBtn.addActionListener(this);
				}
				{
					seOutPutDirBtn = new JButton();
					getContentPane().add(seOutPutDirBtn);
					seOutPutDirBtn.setText("Choose Output Directory");
					seOutPutDirBtn.setBounds(522, 489, 195, 24);
					seOutPutDirBtn.setActionCommand("ChooseOutputDir");
					seOutPutDirBtn.addActionListener(this);
										
				}
				{
					jPanel1 = new JPanel();
					jPanel1.setLayout(null);
					getContentPane().add(jPanel1);
					jPanel1.setBounds(48, 67, 425, 132);
					jPanel1.setBorder(BorderFactory.createTitledBorder("Design Patterns To Detect"));
					{
						adapterCB = new JCheckBox("Adapter",false);
						jPanel1.add(adapterCB);
						adapterCB.setBounds(29, 29, 82, 18);
						adapterCB.setBorder(BorderFactory.createTitledBorder(""));
					}
					{
						compositeCB = new JCheckBox("Composite",false);
						compositeCB.setBounds(306, 30, 93, 18);
						compositeCB.setBorder(BorderFactory.createTitledBorder(""));
					}
					{
						decoratorCB = new JCheckBox("Decorator",false);
					}
					{
						bridgeCB = new JCheckBox();
						bridgeCB.setText("Bridge");
						bridgeCB.setEnabled(false);
					}
					{
						flyweightCB = new JCheckBox();
						flyweightCB.setText("Flyweight");
						flyweightCB.setEnabled(false);
					}
					{
						facadeCB = new JCheckBox();
						jPanel1.add(decoratorCB);
						decoratorCB.setBounds(167, 28, 85, 20);
						jPanel1.add(compositeCB);
						jPanel1.add(flyweightCB);
						flyweightCB.setBounds(29, 63, 88, 20);
						jPanel1.add(bridgeCB);
						bridgeCB.setBounds(167, 63, 71, 20);
						{
							proxyCB = new JCheckBox();
							jPanel1.add(proxyCB);
							jPanel1.add(facadeCB);
							proxyCB.setText("Proxy");
							proxyCB.setEnabled(false);
							proxyCB.setBounds(306, 63, 65, 20);
						}
						facadeCB.setText("Facade");
						facadeCB.setEnabled(false);
						facadeCB.setBounds(29, 98, 77, 20);
					}
				}
				{
					proceedBtn = new JButton();
					getContentPane().add(proceedBtn);
					proceedBtn.setText("Detect");
					proceedBtn.setBounds(500, 84, 118, 24);
					proceedBtn.setActionCommand("ProceedDetection");
					proceedBtn.addActionListener(this);
				}
				{
					jScrollPane1 = new JScrollPane();
					getContentPane().add(jScrollPane1);
					jScrollPane1.setBounds(52, 238, 373, 237);
					{
						consoleTA = new JTextArea();
						jScrollPane1.setViewportView(consoleTA);
						consoleTA.setText("");
						consoleTA.setBounds(52, 245, 373, 237);
					}
				}
				{
					jScrollPane2 = new JScrollPane();
					getContentPane().add(jScrollPane2);
					jScrollPane2.setBounds(522, 220, 218, 255);
					{
						statTA = new JTextArea();
						jScrollPane2.setViewportView(statTA);
						statTA.setBounds(522, 245, 218, 237);
					}
				}
				{
					jLabel1 = new JLabel();
					getContentPane().add(jLabel1);
					jLabel1.setText("Detection Result:");
					jLabel1.setBounds(60, 210, 97, 17);
				}
				{
					jLabel2 = new JLabel();
					getContentPane().add(jLabel2);
					jLabel2.setText("Statistics:");
					jLabel2.setBounds(522, 197, 62, 17);
				}
				{
					outputActionBtn = new JButton();
					getContentPane().add(outputActionBtn);
					outputActionBtn.setText("Output Result in XML Result");
					outputActionBtn.setBounds(522, 532, 195, 24);
					outputActionBtn.setEnabled(false);
					outputActionBtn.setActionCommand("OutputXML");
					outputActionBtn.addActionListener(this);
				}
			}
			this.setSize(800, 600);
			this.setTitle("DPDetector");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);


			prjFileChooser = new JFileChooser();
			prjFileChooser.setFileFilter(new ProjectFileFilter());
			
			outpufPathChooser = new JFileChooser();
			
			detector = new DetectorManager();
			



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ChoosePrjFile")){
			choosePrjFile();
		}else if(e.getActionCommand().equals("ChooseOutputDir")){
			chooseOutputDir();
		}else if(e.getActionCommand().equals("ProceedDetection")){
			proceedDetection();
		}else if(e.getActionCommand().equals("OutputXML")){
			proceedOutputXML();
		}
		
	}
	




	private void choosePrjFile(){
		if (prjFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String sourcepath = prjFileChooser.getSelectedFile().getPath();
			srcPrjTF.setText(sourcepath);
		}
	}
	
	private void chooseOutputDir(){
		outpufPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (outpufPathChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String sourcepath = outpufPathChooser.getSelectedFile().getPath();
			outputDirTF.setText(sourcepath);
			outputActionBtn.setEnabled(true);
		}
	}
	private void proceedDetection(){
		
		String inputPrjPath = srcPrjTF.getText();

		boolean isAdapter=adapterCB.isSelected();
		boolean isComposite = compositeCB.isSelected();
		boolean isDecorator = decoratorCB.isSelected();
		

		try {
	    	detector.clearDetectors();
	    	detector.setPrjFilePath(inputPrjPath);
	    	if(isAdapter){
	    		detector.addDetector(DetectorEnum.ADAPTER);
	    	}
	    	if(isComposite){
	    		detector.addDetector(DetectorEnum.COMPOSITE);
	    	}
	    	if(isDecorator){
	    		detector.addDetector(DetectorEnum.DECORATOR);
	    	}
	    	detector.buildAnalysisModel();
	    	detector.detect();
	    	detector.eliminate();
    	
	    	
	    	String outputConsoleString = detector.outputConsole();
	    	consoleTA.setText(outputConsoleString);

	    	String statisticsString = detector.getStatistics();
	    	statTA.setText(statisticsString);
	    	

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	private void proceedOutputXML() {
		String outputDirPath = outputDirTF.getText();
		detector.setOutputDirPath(outputDirPath);
		detector.outputXMLFile();
		
	}

}
