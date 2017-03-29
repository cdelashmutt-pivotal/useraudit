package io.pivotal.pa;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.pa.domain.Organization;
import io.pivotal.pa.domain.Space;
import io.pivotal.pa.domain.User;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.GetOrganizationUserRolesRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationSpacesRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceUserRolesRequest;
import org.cloudfoundry.client.v2.users.UserResource;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootApplication
public class UserauditApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserauditApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(final CloudFoundryClient client, final ObjectMapper mapper) {
		return (args) -> {

			List<Organization> orgs = client.organizations()
					.list(ListOrganizationsRequest.builder().build())
					.flatMap(response -> Flux.fromIterable(response.getResources()))
					.map(org -> new Organization(org.getMetadata().getId(), org.getEntity().getName()))
					.collectList().block();

			for (Organization org : orgs) {
				org.setUsers(client.organizations()
						.getUserRoles(GetOrganizationUserRolesRequest.builder()
						.organizationId(org.getId())
						.build())
						.flatMap(response -> Flux.fromIterable(response.getResources()))
						.map(resource -> new User(resource.getEntity().getUsername(), resource.getEntity().getOrganizationRoles()))
						.collectList().block()
				);
				org.setSpaces(client.organizations()
						.listSpaces(ListOrganizationSpacesRequest.builder()
						.organizationId(org.getId())
						.build())
						.flatMap(response -> Flux.fromIterable(response.getResources()))
						.map(resource -> new Space(resource.getMetadata().getId(),resource.getEntity().getName()))
						.collectList().block());
				for(Space space : org.getSpaces()) {
					space.setUsers(client.spaces().listUserRoles(
							ListSpaceUserRolesRequest.builder()
									.spaceId(space.getId())
									.build()
					).flatMap(
							spaceUserRoleResource -> Flux.fromIterable(spaceUserRoleResource.getResources())
					).map(spaceUserRoleResource ->
									new User(spaceUserRoleResource.getEntity().getUsername(),
											spaceUserRoleResource.getEntity().getSpaceRoles())
					).collectList().block());
				}
			}

			mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, orgs);

		};
	}

	@Bean
	ObjectMapper mapper(){
		return new ObjectMapper();
	}

	@Bean
	DefaultConnectionContext connectionContext(@Value("${cf.apiHost}") String apiHost) {
		return DefaultConnectionContext.builder()
				.apiHost(apiHost)
				.skipSslValidation(true)
				.build();
	}

	@Bean
	PasswordGrantTokenProvider tokenProvider(@Value("${cf.username}") String username,
											 @Value("${cf.password}") String password) {
		return PasswordGrantTokenProvider.builder()
				.password(password)
				.username(username)
				.build();
	}

	@Bean
	ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
		return ReactorCloudFoundryClient.builder()
				.connectionContext(connectionContext)
				.tokenProvider(tokenProvider)
				.build();
	}

	public static String getUsernameOrId(UserResource user) {
		String username = user.getEntity().getUsername();
		if (username == null) username = user.getMetadata().getId();
		return username;
	}

}
