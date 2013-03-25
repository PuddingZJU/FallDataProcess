

public class SensorData {
	private double _w_Roll;
	private double _w_Pitch;
	private double _w_Yaw;
	private Long _timestamp;
	private double _time;
	private double _a_Roll;
	private double _a_Pitch;
	private double _acc_x;
	private double _acc_y;
	private double _acc_z;
	private double _acc_res;
	public double get_acc_x() {
		return _acc_x;
	}
	public void set_acc_x(double _acc_x) {
		this._acc_x = _acc_x;
	}
	public double get_acc_y() {
		return _acc_y;
	}
	public void set_acc_y(double _acc_y) {
		this._acc_y = _acc_y;
	}
	public double get_acc_z() {
		return _acc_z;
	}
	public void set_acc_z(double _acc_z) {
		this._acc_z = _acc_z;
	}
	public double get_acc_res() {
		return _acc_res;
	}
	public void set_acc_res(double _acc_res) {
		this._acc_res = _acc_res;
	}
	public Long get_timestamp() {
		return _timestamp;
	}
	public void set_timestamp(Long _timestamp) {
		this._timestamp = _timestamp;
	}
	public double get_time() {
		return _time;
	}
	public void set_time(double _time) {
		this._time = _time;
	}

	private double _a_Yaw;
	private double _theta_Roll;
	private double _theta_Pitch;
	private double _theta_Yaw;
	private double w_res;
	private double theta_res;
	private double a_res;
	public double get_a_Roll() {
		return _a_Roll;
	}
	public void set_a_Roll(double _a_Roll) {
		this._a_Roll = _a_Roll;
	}
	public double get_a_Pitch() {
		return _a_Pitch;
	}
	public void set_a_Pitch(double _a_Pitch) {
		this._a_Pitch = _a_Pitch;
	}
	public double get_a_Yaw() {
		return _a_Yaw;
	}
	public void set_a_Yaw(double _a_Yaw) {
		this._a_Yaw = _a_Yaw;
	}
	public double get_theta_Roll() {
		return _theta_Roll;
	}
	public void set_theta_Roll(double _theta_Roll) {
		this._theta_Roll = _theta_Roll;
	}
	public double get_theta_Pitch() {
		return _theta_Pitch;
	}
	public void set_theta_Pitch(double _theta_Pitch) {
		this._theta_Pitch = _theta_Pitch;
	}
	public double get_theta_Yaw() {
		return _theta_Yaw;
	}
	public void set_theta_Yaw(double _theta_Yaw) {
		this._theta_Yaw = _theta_Yaw;
	}
	public void set_theta_res(){
		theta_res = (double)Math.sqrt(_theta_Pitch*_theta_Pitch+_theta_Roll*_theta_Roll+_theta_Yaw*_theta_Yaw);
	}
	public void setA_res() {
		a_res = (double)Math.sqrt(_a_Pitch*_a_Pitch+_a_Roll*_a_Roll+_a_Yaw*_a_Yaw);
	}
	public double getW_res() {
		return w_res;
	}
	public double getTheta_res() {
		return theta_res;
	}
	public double getA_res() {
		return a_res;
	}
	public double get_w_Roll() {
		return _w_Roll;
	}
	public double get_w_Pitch() {
		return _w_Pitch;
	}
	public double get_w_Yaw() {
		return _w_Yaw;
	}
	public void set_w_Roll(double _w_Roll) {
		this._w_Roll = _w_Roll;
	}
	public void set_w_Pitch(double _w_Pitch) {
		this._w_Pitch = _w_Pitch;
	}
	public void set_w_Yaw(double _w_Yaw) {
		this._w_Yaw = _w_Yaw;
	}
	public void setW_res(double w_res) {
		this.w_res =(double) Math.sqrt(_w_Roll*_w_Roll+_w_Pitch*_w_Pitch+_w_Yaw*_w_Yaw);
	}
	public void setTheta_res(double theta_res) {
		this.theta_res = theta_res;
	}
	public void setA_res(double a_res) {
		this.a_res = a_res;
	}
	public SensorData(double values[],Long timestamp){
		if (values.length!=6) {
			System.err.println("Sensor Data is in wrong format!");
			System.exit(-1);
		}
		if (timestamp< 0) {
			System.err.println("TimeStamp value is wrong!");
			System.exit(-1);
		}
		_w_Roll = values[0];
		_w_Pitch = values[1];
		_w_Yaw = values[2];
		_acc_x = values[3];
		_acc_y = values[4];
		_acc_z = values[5];
		_acc_res =  Math.sqrt(_acc_x*_acc_x+_acc_y*_acc_y+_acc_z*_acc_z);
		w_res = (double) Math.sqrt(_w_Roll*_w_Roll+_w_Pitch*_w_Pitch+_w_Yaw*_w_Yaw);
		_timestamp = timestamp;
	}

}
