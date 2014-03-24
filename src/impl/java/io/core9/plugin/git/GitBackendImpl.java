package io.core9.plugin.git;

import java.io.File;
import java.util.Iterator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

@PluginImplementation
public class GitBackendImpl implements GitBackend {

	private Git git;
	private CredentialsProvider cp;
	private String localPath = "data/git";
	
	@Override
	public void addFile(String path, File file) {
		File newFile = new File(localPath + "/" + path);
		newFile.getParentFile().mkdirs();
		file.renameTo(newFile);
		AddCommand ac = git.add();
		ac.addFilepattern(".");
		try {
			ac.call();
		} catch (GitAPIException e) {
			System.err.println("Couldn't add files");
		}
	}

	@Override
	public void commit(String message) {
		CommitCommand commit = git.commit();
        commit.setCommitter("core9test", "jesse@accountshifter.com")
                .setMessage(message);
        try {
			commit.call();
		} catch (GitAPIException e) {
			System.err.println("Couldn't commit");
		}
	}

	@Override
	public void init() {
		String localPath = "data/git";
		FileUtils.delete(new File(localPath));
		String url = "https://trimm@bitbucket.org/core9/plugin-git-test-repo.git";

		// credentials
		cp = new UsernamePasswordCredentialsProvider(
				"core9test", "bicore999");
		// clone
		File dir = new File(localPath);
		CloneCommand cc = new CloneCommand().setCredentialsProvider(cp)
				.setDirectory(dir).setURI(url);
		try {
			git = cc.call();
		} catch (GitAPIException e) {
			System.err.println("Couldn't initialize");
		}
	}

	@Override
	public void push() {
		// push
        PushCommand pc = git.push();
        pc.setCredentialsProvider(cp)
                .setForce(true)
                .setPushAll();
        try {
            Iterator<PushResult> it = pc.call().iterator();
            if(it.hasNext()){
                System.out.println(it.next().toString());
            }
        } catch (GitAPIException e) {
            System.err.println("Couldn't push");
        }
	}

}
