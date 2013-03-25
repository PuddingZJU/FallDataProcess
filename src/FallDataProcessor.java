import java.io.File;
import java.io.IOException;
import java.util.Vector;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FallDataProcessor {
	private Vector<SensorData> buf = null;
	private double[] _FT = new double[7];
	private double _TotalTime;
	private double _AngleStartTime;
	private double _AngleEndTime;
	private double _AngularAccelerationStartTime;
	private double _AngularAccelerationEndTime;
	private int FallStartPosition;
	private int AngleStartPosition;
	private int AngleEndPosition;
	private int AngularAccelerationEndPosition;
	private int AngularAccelerationStartPosition;
	public FallDataProcessor(String filename,double[] FT,
			double TotalTime,double AngleStartTime,double AngleEndTime,double AngularAccelerationStartTime,
			double AngularAcclerationEndTime) {
		_FT = FT;
		_TotalTime = TotalTime;
		_AngleStartTime = AngleStartTime;
		_AngleEndTime = AngleEndTime;
		_AngularAccelerationStartTime = AngularAccelerationStartTime;
		_AngularAccelerationEndTime = AngularAcclerationEndTime;
		File file = new File(filename);
		if (!file.exists()) {
			System.err.println(filename+" is not exist");
			System.exit(-1);
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = builder.parse(file); 
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Element falldataElement  = doc.getDocumentElement();
		//System.out.println(falldataElement.getNodeName());
		NodeList sensordatalList = falldataElement.getElementsByTagName("Data");
		//System.out.println(sensordatalList.getLength());
		buf = new Vector<SensorData>(sensordatalList.getLength());
		double value[] = new double[6];
		long ts = 0;
		for (int i = 0; i < sensordatalList.getLength(); i++) {
			value[0] = Double.parseDouble(sensordatalList.item(i).getAttributes().getNamedItem("Gx").getNodeValue());
			value[1] = Double.parseDouble(sensordatalList.item(i).getAttributes().getNamedItem("Gy").getNodeValue());
			value[2] = Double.parseDouble(sensordatalList.item(i).getAttributes().getNamedItem("Gz").getNodeValue());
			value[3] = Double.parseDouble(sensordatalList.item(i).getAttributes().getNamedItem("Ax").getNodeValue());
			value[4] = Double.parseDouble(sensordatalList.item(i).getAttributes().getNamedItem("Ay").getNodeValue());
			value[5] = Double.parseDouble(sensordatalList.item(i).getAttributes().getNamedItem("Az").getNodeValue());
			ts = Long.parseLong(sensordatalList.item(i).getAttributes().getNamedItem("ts").getNodeValue());
			SensorData gData = new SensorData(value, ts);
			buf.add(gData);
		}
		setAngle();
		setFallStartPosition();
		setAngleEndPosition();
		setAngleStartPosition();
		setAngularAccelerationEndPosition();
		setAngularAccelerationStartPosition();
		setAngleChange();
		SetAngularAccelearion();
	}
	@SuppressWarnings("unchecked")
	public FallDataProcessor(Vector<SensorData> Buffer,double[] FT,
			double TotalTime,double AngleStartTime,double AngleEndTime,double AngularAccelerationStartTime,
			double AngularAcclerationEndTime) {
		buf = (Vector<SensorData>) Buffer.clone();
		_FT = FT;
		_TotalTime = TotalTime;
		_AngleStartTime = AngleStartTime;
		_AngleEndTime = AngleEndTime;
		_AngularAccelerationStartTime = AngularAccelerationStartTime;
		_AngularAccelerationEndTime = AngularAcclerationEndTime;
		setAngle();
		setFallStartPosition();
		setAngleEndPosition();
		setAngleStartPosition();
		setAngularAccelerationEndPosition();
		setAngularAccelerationStartPosition();
		setAngleChange();
		SetAngularAccelearion();
	}
	private void setAngle(){
		for (int i = 1; i < buf.size(); i++) {
			SensorData tmpData = buf.elementAt(i-1);
			double time = (buf.elementAt(i).get_timestamp()-buf.elementAt(i-1).get_timestamp())/1000.0;
			if(time>1 || time<=0.01){
				buf.remove(i);
				i=1;
				continue;
			}
			tmpData.set_theta_Roll(tmpData.get_w_Roll()*time);
			tmpData.set_theta_Pitch(tmpData.get_w_Pitch()*time);
			tmpData.set_theta_Yaw(tmpData.get_w_Yaw()*time);
			tmpData.set_time(time);
			buf.set(i-1, tmpData);
		}
	}
	private void setAngleChange(){
		
		for(int i = AngleStartPosition+1;i<=AngleEndPosition;i++){
			SensorData tmpData = buf.elementAt(i);
			tmpData.set_theta_Pitch(tmpData.get_theta_Pitch()+buf.elementAt(i-1).get_theta_Pitch());
			tmpData.set_theta_Roll(tmpData.get_theta_Roll()+buf.elementAt(i-1).get_theta_Roll());
			tmpData.set_theta_Yaw(tmpData.get_theta_Yaw()+buf.elementAt(i-1).get_theta_Yaw());
			tmpData.set_theta_res();
			buf.set(i, tmpData);
		}
	}
	private void setFallStartPosition() {
		FallStartPosition = 0;
//		for(int i = 0;i<buf.size();i++){
//			FallStartPosition = i;
//			if (buf.elementAt(i).getW_res()>=_FT[1]) {
//				break;
//			}
//		}
	}
	private void setAngleStartPosition() {
		double tmp_time= 0;
		for(int i = FallStartPosition;i>=0;i--){
 			tmp_time+=buf.elementAt(i).get_time();
			if (tmp_time > _AngleStartTime) {
				break;
			}
			AngleStartPosition = i;
		}
	}
	private void setAngleEndPosition() {
		double tmp_time = 0;
		for(int i = FallStartPosition;i<buf.size();i++){
			tmp_time+=buf.elementAt(i).get_time();
			if (tmp_time > _AngleEndTime) {
				break;
			}
			AngleEndPosition = i;
		}
	}
	private void setAngularAccelerationEndPosition() {
		double tmp_time = 0;
		for(int i = FallStartPosition;i<buf.size();i++){
			tmp_time+=buf.elementAt(i).get_time();
			if (tmp_time > _AngularAccelerationEndTime) {
				break;
			}
			AngularAccelerationEndPosition = i;
		}
	}
	private void setAngularAccelerationStartPosition() {
		double tmp_time = 0;
		for(int i = FallStartPosition;i>=0;i--){
			tmp_time+=buf.elementAt(i).get_time();
			if (tmp_time >-_AngularAccelerationStartTime) {
				break;
			}
			AngularAccelerationStartPosition = i;
		}
	}
	private void SetAngularAccelearion(){
		for(int i = AngularAccelerationStartPosition+2  // 2µ„Œ¢∑÷
				;i <= AngularAccelerationEndPosition;i++ ){
			SensorData tmpData = buf.elementAt(i);
			tmpData.set_a_Roll((tmpData.get_w_Roll()- buf.elementAt(i-1).get_w_Roll())/buf.elementAt(i-1).get_time());
			tmpData.set_a_Pitch((tmpData.get_w_Pitch()- buf.elementAt(i-1).get_w_Pitch())/buf.elementAt(i-1).get_time());
			tmpData.set_a_Yaw((tmpData.get_w_Yaw()- buf.elementAt(i-1).get_w_Yaw())/buf.elementAt(i-1).get_time());
			tmpData.setA_res();
			buf.set(i, tmpData);
		}

	}
	public double GetAccelerationPeakResValue(){
		double max =-1;
		for (int i = 0; i <buf.size(); i++) {
			if (Math.abs(buf.elementAt(i).get_acc_res())>=max) {
				max = buf.elementAt(i).get_acc_res();
			}
		}
		return max;
	}
	public double GetAngularVelocityeakResValue(){
		double max =-1;
		for (int i = 0; i <buf.size(); i++) {
			if (Math.abs(buf.elementAt(i).getW_res())>=max) {
				max = buf.elementAt(i).getW_res();
			}
		}
		return max;
	}
	public double GetAngularAccelerationPeakResValue(){
		double max =-1;
		for (int i = AngularAccelerationStartPosition; i <= AngularAccelerationEndPosition; i++) {
			if (Math.abs(buf.elementAt(i).getA_res())>=max) {
				max = buf.elementAt(i).getA_res();
			}
		}
		return max;
	}
	public double GetAngularAccelerationPeakPitchValue(){
		double max =-1;
		
		for (int i = AngularAccelerationStartPosition; i <= AngularAccelerationEndPosition; i++) {
			if (Math.abs(buf.elementAt(i).get_a_Pitch())>=max) {
			
				max = Math.abs(buf.elementAt(i).get_a_Pitch());
			}
		}
		return max;
	}
	public double GetAngularAccelerationPeakRollValue(){
		double max =-1;
		for (int i = AngularAccelerationStartPosition; i <= AngularAccelerationEndPosition; i++) {
			if (Math.abs(buf.elementAt(i).get_a_Roll())>=max) {
				max = Math.abs(buf.elementAt(i).get_a_Roll());
			}
		}
		return max;
	}
	public double GetAngularAccelerationPeakYawValue(){
		double max =-1;
		for (int i = AngularAccelerationStartPosition; i <= AngularAccelerationEndPosition; i++) {
			if (Math.abs(buf.elementAt(i).get_a_Yaw())>=max) {
				max = Math.abs(buf.elementAt(i).get_a_Yaw());
			}
		}
		return max;
	}
	public double GetAngleChangeTotalPitchValue(){
		return buf.elementAt(AngleEndPosition).get_theta_Pitch();
	}
	public double GetAngleChangeTotalRollValue(){
		return buf.elementAt(AngleEndPosition).get_theta_Roll();
	}
	public double GetAngleChangeTotalYawValue(){
		return buf.elementAt(AngleEndPosition).get_theta_Yaw();
	}
	public double GetAngelChangeTotalValue(){
		buf.elementAt(AngleEndPosition).set_theta_res();
		return buf.elementAt(AngleEndPosition).getTheta_res();
	}
	public boolean isFall(){
		int flag = 0;
//		if (GetAccelerationPeakResValue() > _FT[0]) {
//			flag+=1;
//		}
		if (GetAngularVelocityeakResValue() > _FT[1]) {
			flag+=1;
		}
		if (GetAngularAccelerationPeakResValue() > _FT[2]) {
			flag+=1;
		}
		if (GetAngelChangeTotalValue() > _FT[3]) {
			flag+=1;
		}
//		if (GetAngleChangeTotalRollValue()> _FT[4]) {
//			flag+=1;
//		}
//		if (GetAngleChangeTotalPitchValue() > _FT[5]) {
//			flag+=1;
//		}
//		if (GetAngleChangeTotalYawValue()> _FT[6]) {
//			flag+=1;
//		}
		return flag==3;
	}
//	public void SaveToXML(String FileName){
//		File file = new File(FileName);
//		if (!file.getParentFile().exists()) {
//			file.getParentFile().mkdirs();
//		}
//		Document doc = null;
//		try {
//			doc = DocumentBuilderFactory.newInstance().
//					newDocumentBuilder().newDocument();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
//	}
	public String ResultString(){
		return GetAccelerationPeakResValue()+"\t"+GetAngularVelocityeakResValue()+"\t"+GetAngularAccelerationPeakPitchValue()+"\t"
	+GetAngelChangeTotalValue()+"\t"+GetAngleChangeTotalRollValue()+"\t"+GetAngleChangeTotalPitchValue()+"\t"+GetAngleChangeTotalYawValue()+"\n";
	}
}
