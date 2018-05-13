package univ_smb.m1.info803;

import univ_smb.m1.info803.model.Packaging;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.model.Transporter;

public interface ApplicationListener {
    void specificationProcessed(Specification spec);
    void packagingReceived(Packaging packaging);
    void transporterReceived(Transporter transporter);
}
