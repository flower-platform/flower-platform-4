package org.flowerplatform.codesync.github;

import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST;
import static org.flowerplatform.codesync.github.GitHubConstants.REPOSITORY;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.adapter.NodeModelAdapterAncestor;
import org.flowerplatform.codesync.adapter.NodeModelAdapterLeft;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.github.adapter.GitHubCommentModelAdapter;
import org.flowerplatform.codesync.github.adapter.GitHubCommitCommentModelAdapter;
import org.flowerplatform.codesync.github.adapter.GitHubCommitFileModelAdapter;
import org.flowerplatform.codesync.github.adapter.GitHubPullRequestModelAdapter;
import org.flowerplatform.codesync.github.adapter.GitHubRepositoryModelAdapter;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommentFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommitCommentFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommitFileFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubPullRequestFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubRepositoryFeatureProvider;
import org.flowerplatform.codesync.github.remote.GitHubOperationsService;
import org.flowerplatform.codesync.github.type_provider.GitHubTypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncGitHubPlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncGitHubPlugin INSTANCE;
	
	public static CodeSyncGitHubPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("gitHubOperationsService", new GitHubOperationsService());
		
		CodeSyncPlugin.getInstance().addTypeProvider(GitHubConstants.GITHUB, new GitHubTypeProvider());
		
		createTypeDescriptor(REPOSITORY, new GitHubRepositoryModelAdapter(), new GitHubRepositoryFeatureProvider());
		createTypeDescriptor(PULL_REQUEST, new GitHubPullRequestModelAdapter(), new GitHubPullRequestFeatureProvider());
		createTypeDescriptor(COMMIT_FILE, new GitHubCommitFileModelAdapter(), new GitHubCommitFileFeatureProvider());
		createTypeDescriptor(COMMIT_COMMENT, new GitHubCommitCommentModelAdapter(), new GitHubCommitCommentFeatureProvider());
		createTypeDescriptor(COMMENT, new GitHubCommentModelAdapter(), new GitHubCommentFeatureProvider());
	}
	
	private void createTypeDescriptor(String type, AbstractModelAdapter rightModelAdapter, FeatureProvider featureProvider) {
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type)
		.addCategory("category.codesync")
		.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_ANCESTOR, new NodeModelAdapterAncestor())
		.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_LEFT, new NodeModelAdapterLeft())
		.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_RIGHT, rightModelAdapter)
		.addSingleController(FeatureProvider.FEATURE_PROVIDER, featureProvider);
	}

	public GitHubClient getClient() {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(getOAuth2Token());
		return client;
	}
	
	private String getOAuth2Token() {
		return null; // get from properties?
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing yet
	}

}
