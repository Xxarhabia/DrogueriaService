package com.msara.servicio;

import com.msara.servicio.domain.entities.PermissionEntity;
import com.msara.servicio.domain.entities.RoleEntity;
import com.msara.servicio.domain.entities.UserEntity;
import com.msara.servicio.domain.enums.RoleEnum;
import com.msara.servicio.domain.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;


@SpringBootApplication
public class DrogueriaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrogueriaServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(UserRepository userRepository) {
//		return args -> {
//			PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
//			PermissionEntity readPermission = PermissionEntity.builder().name("READ").build();
//			PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
//			PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();
//
//			RoleEntity roleAdmin = RoleEntity.builder()
//					.roleEnum(RoleEnum.ADMIN)
//					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
//					.build();
//
//			UserEntity admin = UserEntity.builder()
//					.name("Admin")
//					.email("admin@admin.com")
//					.password("$2a$10$Xz3JHq1cPkBsGhwcfd4x9uQpzs3ZeagZKF7AfmDmB80zO9qAk8RoG")
//					.address("drogueria")
//					.isEnabled(true)
//					.accountNoExpired(true)
//					.accountNoLocked(true)
//					.credentialNoExpired(true)
//					.roles(Set.of(roleAdmin))
//					.build();
//
//			userRepository.save(admin);
//		};
//	}

}
