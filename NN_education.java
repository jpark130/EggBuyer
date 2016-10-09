import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NN_education {
	N_education[] input_layer;
	N_education[] hidden_layer;
	N_education[] output_layer;
	double ss = 0.5;
	
	public static void main(String[] args) throws IOException {
		File path = new File(args[0]);
		FileInputStream fis = new FileInputStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));	
		
		String line = reader.readLine();
		List<String[]> data = new ArrayList<String[]>();
		
		File path2 = new File(args[1]);
		FileInputStream fis2 = new FileInputStream(path2);
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(fis2));	
		
		while((line = reader.readLine()) != null) {
			String[] data_s = line.split(",");
			data.add(data_s);
		}
		double[][] data_normal = new double[data.size()][data.get(0).length - 1];
		double val;
		double[] t = new double[data.size()];
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(0).length; j++) {
				val = Double.valueOf(data.get(i)[j]) / 100;
				if (j < 5) {
					data_normal[i][j] = val;	
				} else {
					t[i] = val;
				}
			}
		}
		int o_edges = 9;
		int h_edges = 5;
		NN_education network = new NN_education();
		network.topology(o_edges, h_edges);
		network.Backpropogation(data_normal, t);
		System.out.println("TRAINING COMPLETED! NOW PREDICTING.");
		
		List<String[]> data_d = new ArrayList<String[]>();
		reader2.readLine();
		while((line = reader2.readLine()) != null) {
			String[] data_ds = line.split(",");
			data_d.add(data_ds);
		}
		double[][] data_dev = new double[data_d.size()][data_d.get(0).length];
		for (int i = 0; i < data_d.size(); i++) {
			for (int j = 0; j < data_d.get(0).length; j++) {
				val = Double.valueOf(data_d.get(i)[j]) / 100;
				data_dev[i][j] = val;
			}
		}
		
		for(double[] xi : data_dev) {
			network.predict(xi);
			System.out.println(network.output_layer[0].output * 100);
		}
		
//		File path3 = new File("/Users/tjkim05/Documents/workspace/10-601/hw7/education_dev_keys.txt");
//		FileInputStream fis3 = new FileInputStream(path3);
//		BufferedReader reader3 = new BufferedReader(new InputStreamReader(fis3));	
//		List<Double> data_k = new ArrayList<Double>();
//		
//		while((line = reader3.readLine()) != null) {
//			if (line.equals("yes")) {
//				data_k.add(1.0);
//			} else {
//				data_k.add(0.0);
//			}
//		}
//		int err = 0;
//		double err_val = 0;
//		for (int i = 0; i < prediction.length; i++) {
//			System.out.println(prediction[i]);
//			if (prediction[i] != data_k.get(i)) {
//				err_val += Math.abs(prediction[i] - data_k.get(i));
//				err++;
//			}
//		}
//		System.out.println(err_val);
	}

	public void topology(int o_edges, int h_edges) {
		this.output_layer = new N_education[1];
		for (int i = 0; i < 1; i++) {
			this.output_layer[i] = new N_education(o_edges, 0);
		}
		this.hidden_layer = new N_education[o_edges];
		for (int i = 0; i < o_edges; i++) {
			this.hidden_layer[i] = new N_education(h_edges , 1);
		}
	}

	public double[] predict(double[] x) {
		double[] x2 = new double[this.hidden_layer.length];
		N_education neuron;
		for (int i = 0; i < this.hidden_layer.length; i++) {
			neuron = this.hidden_layer[i];
			x2[i] = neuron.output(x, neuron.weight);
		}
		double[] output = new double[this.output_layer.length];
		for (int i = 0; i < this.output_layer.length; i++) {
			neuron = this.output_layer[i];
			output[i] = neuron.output(x2, neuron.weight);
		}
		return output;
	}
	
	public void Backpropogation(double[][] data, double[] t) {
		double[] delta_k = new double[this.output_layer.length];
		double[] delta_h = new double[this.hidden_layer.length];
		double o_k;
		double o_h;
		N_education op_n;
		N_education h_n;
		double xi;
		double w_hk;
		int i = 0;
		double mse = 1;
		
		double[] output = new double[t.length];
		double val;
		
		double cur_mse = 1;
		double prev_mse = 1;
		while ((prev_mse == 1 && cur_mse == 1) || (prev_mse - cur_mse) > 0.0000000001) {
			i = 0;
			for (double[] x : data) {
				predict(x);
				val = this.output_layer[0].output;
				output[i] = val;
				op_n = this.output_layer[0];
				o_k = op_n.output;
				delta_k[0] = o_k * (1 - o_k) * (t[i] - o_k);
				for (int l = 0; l < this.hidden_layer.length; l++) {
					h_n = this.hidden_layer[l];
					o_h = h_n.output;
					w_hk = this.output_layer[0].weight[l + 1];
					delta_h[l] = o_h * (1 - o_h) * (w_hk * delta_k[0]);
					for (int d = 0; d < x.length; d++) {
						xi = x[d];
						h_n.weight[d + 1] += ss * delta_h[l] * xi;
					}
					h_n.weight[0] += ss * delta_h[l] ;
					output_layer[0].weight[l + 1] += ss * delta_k[0] * o_h;
				}
				i++;
			}
			mse = MSE(output, t);
			prev_mse = cur_mse;
			cur_mse = mse;
			System.out.println(mse);
		}	
	}
	public double MSE(double[] o, double[] t) {
		double sum = 0;
		for (int i = 0; i < o.length; i++) {
			sum += Math.pow(o[i] - t[i], 2);
		}
//		return sum;
		return sum / o.length;
	}
}
