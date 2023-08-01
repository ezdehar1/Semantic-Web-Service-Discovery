package t33_a;

public class RecomService  {
	
	public service s;
	public double sim;


public  RecomService( service se, double ss)
{
	s=se;
	sim=ss;
}

public double simV()
{
	return sim;
}

/*@Override
public int compareTo(RecomService r)

{
	//return sim.compareTo(r.sim);
	return new Double(simV()).compareTo(r.simV());
}*/

}