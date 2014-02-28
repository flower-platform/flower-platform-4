package org.flowerplatform.codesync.github;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.AbstractModelAdapter;
import org.flowerplatform.codesync.adapter.NodeModelAdapterAncestor;
import org.flowerplatform.codesync.adapter.NodeModelAdapterLeft;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.github.adapter.ClientModelAdapter;
import org.flowerplatform.codesync.github.adapter.CommentModelAdapter;
import org.flowerplatform.codesync.github.adapter.CommitCommentModelAdapter;
import org.flowerplatform.codesync.github.adapter.CommitFileModelAdapter;
import org.flowerplatform.codesync.github.adapter.PullRequestModelAdapter;
import org.flowerplatform.codesync.github.feature_provider.ClientFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.CommentFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.CommitCommentFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.CommitFileFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.PullRequestFeatureProvider;
import org.flowerplatform.codesync.github.remote.GitHubOperationsService;
import org.flowerplatform.codesync.github.type_provider.GitHubTypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncGitHubPlugin extends AbstractFlowerJavaPlugin {

	public static final String GITHUB = "gitHub";
	
	protected static CodeSyncGitHubPlugin INSTANCE;
	
	public static CodeSyncGitHubPlugin getInstance() {
		return INSTANCE;
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("gitHubOperationsService", new GitHubOperationsService());
		
		CodeSyncPlugin.getInstance().addTypeProvider(GITHUB, new GitHubTypeProvider());
		
		createTypeDescriptor(GitHubTypeProvider.CLIENT, new ClientModelAdapter(), new ClientFeatureProvider());
		createTypeDescriptor(GitHubTypeProvider.PULL_REQUEST, new PullRequestModelAdapter(), new PullRequestFeatureProvider());
		createTypeDescriptor(GitHubTypeProvider.COMMIT_FILE, new CommitFileModelAdapter(), new CommitFileFeatureProvider());
		createTypeDescriptor(GitHubTypeProvider.COMMIT_COMMENT, new CommitCommentModelAdapter(), new CommitCommentFeatureProvider());
		createTypeDescriptor(GitHubTypeProvider.COMMENT, new CommentModelAdapter(), new CommentFeatureProvider());
	}
	
	private void createTypeDescriptor(String type, AbstractModelAdapter rightModelAdapter, FeatureProvider featureProvider) {
		TypeDescriptor descriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type);
		descriptor.addCategory("category.codesync");
		descriptor.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_ANCESTOR, new NodeModelAdapterAncestor());
		descriptor.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_LEFT, new NodeModelAdapterLeft());
		descriptor.addSingleController(AbstractModelAdapter.MODEL_ADAPTER_RIGHT, rightModelAdapter);
		descriptor.addSingleController(FeatureProvider.FEATURE_PROVIDER, featureProvider);
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
