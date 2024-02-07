package ma.digiup.assignement.repository;

import ma.digiup.assignement.domain.AuditDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditDepositRepository extends JpaRepository<AuditDeposit, Long> {
}
