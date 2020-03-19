package twitter.opinion.mining.response.pojo;

public class AnalysedData {

	public AnalysedData( String name,Integer score) {
		this.name=name;
		this.score=score;
	}
	private String name;
	private Integer score;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
