package ma.digiup.assignement.web;

import ma.digiup.assignement.domain.MoneyDeposit;
import ma.digiup.assignement.domain.Utilisateur;
import ma.digiup.assignement.repository.MoneyDepositRepository;
import ma.digiup.assignement.repository.UtilisateurRepository;
import ma.digiup.assignement.domain.Compte;
import ma.digiup.assignement.domain.Transfer;
import ma.digiup.assignement.dto.TransferDto;
import ma.digiup.assignement.exceptions.CompteNonExistantException;
import ma.digiup.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.digiup.assignement.exceptions.TransactionException;
import ma.digiup.assignement.repository.CompteRepository;
import ma.digiup.assignement.repository.TransferRepository;
import ma.digiup.assignement.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController("/transfers")
class TransferController {

    public static final int MONTANT_MAXIMAL = 10000;

    Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AuditService auditService;
    @Autowired
private MoneyDepositRepository moneyDepositRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    TransferController(UtilisateurRepository re3) {
        this.utilisateurRepository = re3;
    }

    @GetMapping("listDesTransferts")
    List<Transfer> loadAll() {
        LOGGER.info("Lister des utilisateurs");
        var all = transferRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return CollectionUtils.isEmpty(all) ? all : null;
        }
    }

    @GetMapping("listOfAccounts")
    List<Compte> loadAllCompte() {
        List<Compte> all = compteRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @GetMapping("lister_utilisateurs")
    List<Utilisateur> loadAllUtilisateur() {
        List<Utilisateur> all = utilisateurRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

        @PostMapping("/executerTransfers")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransferDto transferDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {
        Compte c1 = compteRepository.findByNrCompte(transferDto.getNrCompteEmetteur());
        Compte f12 = compteRepository
                .findByNrCompte(transferDto.getNrCompteBeneficiaire());

        if (c1 == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }
        if (f12 == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }
        if (transferDto.getMontant() == null || transferDto.getMontant().intValue() == 0) {
            throw new TransactionException("Montant vide");
        }

        if (transferDto.getMontant().intValue() < 10 || transferDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            throw new TransactionException("Montant invalide");
        }
        if (transferDto.getMotif() == null || transferDto.getMotif().isEmpty()) {
            throw new TransactionException("Motif vide");
        }

        if (c1.getSolde().intValue() - transferDto.getMontant().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
        }

        if (c1.getSolde().intValue() - transferDto.getMontant().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
        }

        c1.setSolde(c1.getSolde().subtract(transferDto.getMontant()));
        compteRepository.save(c1);

        f12
                .setSolde(new BigDecimal(f12.getSolde().intValue() + transferDto.getMontant().intValue()));
        compteRepository.save(f12);

        Transfer transfer = new Transfer();
        transfer.setDateExecution(transferDto.getDate());
        transfer.setCompteBeneficiaire(f12);
        transfer.setCompteEmetteur(c1);
        transfer.setMontantTransfer(transferDto.getMontant());

        transferRepository.save(transfer);

        auditService.auditTransfer("Transfer depuis " + transferDto.getNrCompteEmetteur() + " vers " + transferDto
                        .getNrCompteBeneficiaire() + " d'un montant de " + transferDto.getMontant()
                        .toString());
        if (transferDto.getMontant().intValue() > 0) {
            performDeposit(transferDto);
        }
    }
    // New method for deposit functionality
    private void performDeposit(TransferDto transferDto) throws CompteNonExistantException, TransactionException {
        Compte compteBeneficiaire = compteRepository.findByNrCompte(transferDto.getNrCompteBeneficiaire());

        // Check if the beneficiary account exists
        if (compteBeneficiaire == null) {
            throw new CompteNonExistantException("Compte bénéficiaire non existant");
        }

        // Check if the Montant is within the valid range
        if (transferDto.getMontant().intValue() <= 0 || transferDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            throw new TransactionException("Montant invalide pour le dépôt");
        }

        // Update
        compteBeneficiaire.setSolde(compteBeneficiaire.getSolde().add(transferDto.getMontant()));
        compteRepository.save(compteBeneficiaire);

        // Create new
        MoneyDeposit moneyDeposit = new MoneyDeposit();
        moneyDeposit.setMontant(transferDto.getMontant());
        moneyDeposit.setDateExecution(new Date());
        moneyDeposit.setCompteBeneficiaire(compteBeneficiaire);
        moneyDeposit.setMotifDeposit(transferDto.getMotif());
        moneyDeposit.setNom_prenom_emetteur(transferDto.getNrCompteEmetteur());
        moneyDepositRepository.save(moneyDeposit);

        // Log the deposit event
        auditService.auditDeposit("Dépôt sur le compte " + transferDto.getNrCompteBeneficiaire() +
                " d'un montant de " + transferDto.getMontant().toString());
    }
    private void save(Transfer Transfer) {
        transferRepository.save(Transfer);
    }
}
