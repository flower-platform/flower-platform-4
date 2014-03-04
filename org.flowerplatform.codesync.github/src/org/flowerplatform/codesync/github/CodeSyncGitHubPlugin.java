package org.flowerplatform.codesync.github;

import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.ICON;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMENT_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_COMMIT_ID;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_PATH;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_COMMENT_POSITION;
import static org.flowerplatform.codesync.github.GitHubConstants.COMMIT_FILE;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_COMMENTS;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_COMMIT_FILES;
import static org.flowerplatform.codesync.github.GitHubConstants.CONTAINMENT_PULL_REQUESTS;
import static org.flowerplatform.codesync.github.GitHubConstants.GITHUB;
import static org.flowerplatform.codesync.github.GitHubConstants.IMG_COMMENT;
import static org.flowerplatform.codesync.github.GitHubConstants.IMG_FILE;
import static org.flowerplatform.codesync.github.GitHubConstants.IMG_PULL_REQUEST;
import static org.flowerplatform.codesync.github.GitHubConstants.IMG_REPOSITORY;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_BASE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_BODY;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_HEAD;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE_CLOSED;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_STATE_OPEN;
import static org.flowerplatform.codesync.github.GitHubConstants.PULL_REQUEST_TITLE;
import static org.flowerplatform.codesync.github.GitHubConstants.REPOSITORY;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.remote.AddChildDescriptor.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.DROP_DOWN_LIST;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.NUMBER;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

import java.util.Arrays;

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
import org.flowerplatform.codesync.github.controller.GitHubCommitFileIconPropertyProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommentFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommitCommentFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubCommitFileFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubPullRequestFeatureProvider;
import org.flowerplatform.codesync.github.feature_provider.GitHubRepositoryFeatureProvider;
import org.flowerplatform.codesync.github.remote.GitHubOperationsService;
import org.flowerplatform.codesync.github.type_provider.GitHubTypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;
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
		
		CodeSyncPlugin.getInstance().addTypeProvider(GITHUB, new GitHubTypeProvider());
		
		createTypeDescriptor(REPOSITORY, new GitHubRepositoryModelAdapter(), new GitHubRepositoryFeatureProvider())
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_REPOSITORY)))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(PULL_REQUEST)
				.setLabelAs(getMessage("github.pullRequest")).setIconAs(getResourceUrl(IMG_PULL_REQUEST)));
		
		createTypeDescriptor(PULL_REQUEST, new GitHubPullRequestModelAdapter(), new GitHubPullRequestFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(CONTAINMENT_PULL_REQUESTS))
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_PULL_REQUEST)))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(PULL_REQUEST_TITLE).setReadOnlyAs(false))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(PULL_REQUEST_BODY).setReadOnlyAs(false))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(PULL_REQUEST_STATE).setTypeAs(DROP_DOWN_LIST).setReadOnlyAs(false)
				.setPossibleValuesAs(Arrays.asList(PULL_REQUEST_STATE_OPEN, PULL_REQUEST_STATE_CLOSED)))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(PULL_REQUEST_BASE).setReadOnlyAs(false))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(PULL_REQUEST_HEAD).setReadOnlyAs(false))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(COMMIT_COMMENT)
				.setLabelAs(getMessage("github.commitComment")).setIconAs(getResourceUrl(IMG_COMMENT)))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(COMMENT)
				.setLabelAs(getMessage("github.comment")).setIconAs(getResourceUrl(IMG_COMMENT)));
		
		createTypeDescriptor(COMMIT_FILE, new GitHubCommitFileModelAdapter(), new GitHubCommitFileFeatureProvider())
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(CONTAINMENT_COMMIT_FILES))
		.addAdditiveController(PROPERTIES_PROVIDER, new GitHubCommitFileIconPropertyProvider(ICON, getResourcePath(IMG_FILE)));
		
		String categoryComment = "category.gitHubComment";
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(categoryComment)
		.addSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, new MemberOfChildCategoryDescriptor(CONTAINMENT_COMMENTS))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(COMMENT_BODY).setReadOnlyAs(false))
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(ICON, getResourcePath(IMG_COMMENT)));
		
		createTypeDescriptor(COMMIT_COMMENT, new GitHubCommitCommentModelAdapter(), new GitHubCommitCommentFeatureProvider())
		.addCategory(categoryComment)
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(COMMIT_COMMENT_COMMIT_ID).setReadOnlyAs(false))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(COMMIT_COMMENT_PATH).setReadOnlyAs(false))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(COMMIT_COMMENT_POSITION).setTypeAs(NUMBER).setReadOnlyAs(false));
		
		createTypeDescriptor(COMMENT, new GitHubCommentModelAdapter(), new GitHubCommentFeatureProvider())
		.addCategory(categoryComment);
	}
	
	private TypeDescriptor createTypeDescriptor(String type, AbstractModelAdapter rightModelAdapter, FeatureProvider featureProvider) {
		return CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(type)
		.addCategory("category.codeSync")
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

}
