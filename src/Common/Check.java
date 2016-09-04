package Common;
import java.io.FileNotFoundException;

public interface Check {
	public void generate() throws FileNotFoundException;
	public void check();
}