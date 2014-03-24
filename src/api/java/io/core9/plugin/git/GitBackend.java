package io.core9.plugin.git;

import java.io.File;

import io.core9.core.plugin.Core9Plugin;

public interface GitBackend extends Core9Plugin {
	
	void commit(String message);

	void push();

	void init();

	void addFile(String path, File file);

	
}
