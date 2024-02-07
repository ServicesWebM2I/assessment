package ma.digiup.assignement.repository;

import ma.digiup.assignement.domain.AuditDeposit;
import ma.digiup.assignement.domain.AuditTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditTransferRepository extends JpaRepository<AuditTransfer, Long> {
}
