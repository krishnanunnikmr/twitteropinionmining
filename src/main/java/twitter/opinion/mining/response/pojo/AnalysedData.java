package twitter.opinion.mining.response.pojo;

public class AnalysedData {

	public AnalysedData( String name,Double score) {
		this.name=name;
		this.score=score;
	}
	private String name;
	private Double score;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	
}
