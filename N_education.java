import java.util.Random;

public class N_education {
	int edges;
	double input;
	double output;	
	double[] weight;
	double delta;
	
	public N_education() {
	}
	
	public N_education(int edges,int x) {
		this.edges = edges;
		initWeight(edges, x);
	}

	public void initWeight(int edges, int x) {
		this.weight = new double[edges + 1];
		Random rg = new Random();
		double rand;
		for (int i = 0; i < edges + 1; i++) {
			rand = rg.nextDouble();
			this.weight[i] = rand - 0.5;
		}
		if (x == 0) {
			this.weight[0] = 0;
		}
	}
	
	public double sigmoid(double x) {
		double result = (double) (1 / (1 + Math.exp(-x)));
		return result;
	}
	
	public double output(double[] x_input, double[] weight) {
	

		double result = weight[0];
		for (int i = 0; i < x_input.length; i++) {
			result += x_input[i] * weight[i + 1];
		}
		this.input = result;
		this.output = sigmoid(result);
		return this.output;
	}
}
