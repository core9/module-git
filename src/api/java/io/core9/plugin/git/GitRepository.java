package io.core9.plugin.git;

public interface GitRepository {

	String getLocalPath();

	void setLocalPath(String localPath);

	String getOrigin();

	void setOrigin(String origin);
	
	String getUsername();
	
	void setUsername(String username);
	
	String getPassword();
	
	void setPassword(String password);
	
	boolean exists();

}
