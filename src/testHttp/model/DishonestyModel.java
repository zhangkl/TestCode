package testHttp.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/3/17
 * Time: 17:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "CRED_DISHONESTY")
public class DishonestyModel {
    private Integer iid;
    private String sstdstg;
    private String sstdstl;
    private Timestamp dupdateTime;
    private String sloc;
    private Date dlastmod;
    private String schangefreq;
    private String spriority;
    private String ssitelink;
    private String siname;
    private String stype;
    private String scardnum;
    private String scasecode;
    private Integer iage;
    private String ssexy;
    private String sfocusnumber;
    private String sareaname;
    private String sbusinessentity;
    private String scourtname;
    private String sduty;
    private String sperformance;
    private String sdisrupttypename;
    private Date dpublishdate;
    private String spartytypename;
    private String sgistid;
    private Date dregdate;
    private String sgistunit;
    private String sperformedpart;
    private String sunperformpart;
    private String spublishdatestamp;
    private String ssiteid;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cred_dishonesty")
    @SequenceGenerator(name = "seq_cred_dishonesty", allocationSize = 1, initialValue = 1, sequenceName = "seq_cred_dishonesty")
    @Id
    @Column(name = "IID")
    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    @Basic
    @Column(name = "SSTDSTG")
    public String getSstdstg() {
        return sstdstg;
    }

    public void setSstdstg(String sstdstg) {
        this.sstdstg = sstdstg;
    }

    @Basic
    @Column(name = "SSTDSTL")
    public String getSstdstl() {
        return sstdstl;
    }

    public void setSstdstl(String sstdstl) {
        this.sstdstl = sstdstl;
    }

    @Basic
    @Column(name = "DUPDATE_TIME")
    public Timestamp getDupdateTime() {
        return dupdateTime;
    }

    public void setDupdateTime(Timestamp dupdateTime) {
        this.dupdateTime = dupdateTime;
    }

    @Basic
    @Column(name = "SLOC")
    public String getSloc() {
        return sloc;
    }

    public void setSloc(String sloc) {
        this.sloc = sloc;
    }

    @Basic
    @Column(name = "DLASTMOD")
    public Date getDlastmod() {
        return dlastmod;
    }

    public void setDlastmod(Date dlastmod) {
        this.dlastmod = dlastmod;
    }

    @Basic
    @Column(name = "SCHANGEFREQ")
    public String getSchangefreq() {
        return schangefreq;
    }

    public void setSchangefreq(String schangefreq) {
        this.schangefreq = schangefreq;
    }

    @Basic
    @Column(name = "SPRIORITY")
    public String getSpriority() {
        return spriority;
    }

    public void setSpriority(String spriority) {
        this.spriority = spriority;
    }

    @Basic
    @Column(name = "SSITELINK")
    public String getSsitelink() {
        return ssitelink;
    }

    public void setSsitelink(String ssitelink) {
        this.ssitelink = ssitelink;
    }

    @Basic
    @Column(name = "SINAME")
    public String getSiname() {
        return siname;
    }

    public void setSiname(String siname) {
        this.siname = siname;
    }

    @Basic
    @Column(name = "STYPE")
    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    @Basic
    @Column(name = "SCARDNUM")
    public String getScardnum() {
        return scardnum;
    }

    public void setScardnum(String scardnum) {
        this.scardnum = scardnum;
    }

    @Basic
    @Column(name = "SCASECODE")
    public String getScasecode() {
        return scasecode;
    }

    public void setScasecode(String scasecode) {
        this.scasecode = scasecode;
    }

    @Basic
    @Column(name = "IAGE")
    public Integer getIage() {
        return iage;
    }

    public void setIage(Integer iage) {
        this.iage = iage;
    }

    @Basic
    @Column(name = "SSEXY")
    public String getSsexy() {
        return ssexy;
    }

    public void setSsexy(String ssexy) {
        this.ssexy = ssexy;
    }

    @Basic
    @Column(name = "SFOCUSNUMBER")
    public String getSfocusnumber() {
        return sfocusnumber;
    }

    public void setSfocusnumber(String sfocusnumber) {
        this.sfocusnumber = sfocusnumber;
    }

    @Basic
    @Column(name = "SAREANAME")
    public String getSareaname() {
        return sareaname;
    }

    public void setSareaname(String sareaname) {
        this.sareaname = sareaname;
    }

    @Basic
    @Column(name = "SBUSINESSENTITY")
    public String getSbusinessentity() {
        return sbusinessentity;
    }

    public void setSbusinessentity(String sbusinessentity) {
        this.sbusinessentity = sbusinessentity;
    }

    @Basic
    @Column(name = "SCOURTNAME")
    public String getScourtname() {
        return scourtname;
    }

    public void setScourtname(String scourtname) {
        this.scourtname = scourtname;
    }

    @Basic
    @Column(name = "SDUTY")
    public String getSduty() {
        return sduty;
    }

    public void setSduty(String sduty) {
        this.sduty = sduty;
    }

    @Basic
    @Column(name = "SPERFORMANCE")
    public String getSperformance() {
        return sperformance;
    }

    public void setSperformance(String sperformance) {
        this.sperformance = sperformance;
    }

    @Basic
    @Column(name = "SDISRUPTTYPENAME")
    public String getSdisrupttypename() {
        return sdisrupttypename;
    }

    public void setSdisrupttypename(String sdisrupttypename) {
        this.sdisrupttypename = sdisrupttypename;
    }

    @Basic
    @Column(name = "DPUBLISHDATE")
    public Date getDpublishdate() {
        return dpublishdate;
    }

    public void setDpublishdate(Date dpublishdate) {
        this.dpublishdate = dpublishdate;
    }

    @Basic
    @Column(name = "SPARTYTYPENAME")
    public String getSpartytypename() {
        return spartytypename;
    }

    public void setSpartytypename(String spartytypename) {
        this.spartytypename = spartytypename;
    }

    @Basic
    @Column(name = "SGISTID")
    public String getSgistid() {
        return sgistid;
    }

    public void setSgistid(String sgistid) {
        this.sgistid = sgistid;
    }

    @Basic
    @Column(name = "DREGDATE")
    public Date getDregdate() {
        return dregdate;
    }

    public void setDregdate(Date dregdate) {
        this.dregdate = dregdate;
    }

    @Basic
    @Column(name = "SGISTUNIT")
    public String getSgistunit() {
        return sgistunit;
    }

    public void setSgistunit(String sgistunit) {
        this.sgistunit = sgistunit;
    }

    @Basic
    @Column(name = "SPERFORMEDPART")
    public String getSperformedpart() {
        return sperformedpart;
    }

    public void setSperformedpart(String sperformedpart) {
        this.sperformedpart = sperformedpart;
    }

    @Basic
    @Column(name = "SUNPERFORMPART")
    public String getSunperformpart() {
        return sunperformpart;
    }

    public void setSunperformpart(String sunperformpart) {
        this.sunperformpart = sunperformpart;
    }

    @Basic
    @Column(name = "SPUBLISHDATESTAMP")
    public String getSpublishdatestamp() {
        return spublishdatestamp;
    }

    public void setSpublishdatestamp(String spublishdatestamp) {
        this.spublishdatestamp = spublishdatestamp;
    }

    @Basic
    @Column(name = "SSITEID")
    public String getSsiteid() {
        return ssiteid;
    }

    public void setSsiteid(String ssiteid) {
        this.ssiteid = ssiteid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DishonestyModel that = (DishonestyModel) o;

        if (dlastmod != null ? !dlastmod.equals(that.dlastmod) : that.dlastmod != null) return false;
        if (dpublishdate != null ? !dpublishdate.equals(that.dpublishdate) : that.dpublishdate != null) return false;
        if (dregdate != null ? !dregdate.equals(that.dregdate) : that.dregdate != null) return false;
        if (dupdateTime != null ? !dupdateTime.equals(that.dupdateTime) : that.dupdateTime != null) return false;
        if (iage != null ? !iage.equals(that.iage) : that.iage != null) return false;
        if (iid != null ? !iid.equals(that.iid) : that.iid != null) return false;
        if (sareaname != null ? !sareaname.equals(that.sareaname) : that.sareaname != null) return false;
        if (sbusinessentity != null ? !sbusinessentity.equals(that.sbusinessentity) : that.sbusinessentity != null)
            return false;
        if (scardnum != null ? !scardnum.equals(that.scardnum) : that.scardnum != null) return false;
        if (scasecode != null ? !scasecode.equals(that.scasecode) : that.scasecode != null) return false;
        if (schangefreq != null ? !schangefreq.equals(that.schangefreq) : that.schangefreq != null) return false;
        if (scourtname != null ? !scourtname.equals(that.scourtname) : that.scourtname != null) return false;
        if (sdisrupttypename != null ? !sdisrupttypename.equals(that.sdisrupttypename) : that.sdisrupttypename != null)
            return false;
        if (sduty != null ? !sduty.equals(that.sduty) : that.sduty != null) return false;
        if (sfocusnumber != null ? !sfocusnumber.equals(that.sfocusnumber) : that.sfocusnumber != null) return false;
        if (sgistid != null ? !sgistid.equals(that.sgistid) : that.sgistid != null) return false;
        if (sgistunit != null ? !sgistunit.equals(that.sgistunit) : that.sgistunit != null) return false;
        if (siname != null ? !siname.equals(that.siname) : that.siname != null) return false;
        if (sloc != null ? !sloc.equals(that.sloc) : that.sloc != null) return false;
        if (spartytypename != null ? !spartytypename.equals(that.spartytypename) : that.spartytypename != null)
            return false;
        if (sperformance != null ? !sperformance.equals(that.sperformance) : that.sperformance != null) return false;
        if (sperformedpart != null ? !sperformedpart.equals(that.sperformedpart) : that.sperformedpart != null)
            return false;
        if (spriority != null ? !spriority.equals(that.spriority) : that.spriority != null) return false;
        if (spublishdatestamp != null ? !spublishdatestamp.equals(that.spublishdatestamp) : that.spublishdatestamp != null)
            return false;
        if (ssexy != null ? !ssexy.equals(that.ssexy) : that.ssexy != null) return false;
        if (ssiteid != null ? !ssiteid.equals(that.ssiteid) : that.ssiteid != null) return false;
        if (ssitelink != null ? !ssitelink.equals(that.ssitelink) : that.ssitelink != null) return false;
        if (sstdstg != null ? !sstdstg.equals(that.sstdstg) : that.sstdstg != null) return false;
        if (sstdstl != null ? !sstdstl.equals(that.sstdstl) : that.sstdstl != null) return false;
        if (stype != null ? !stype.equals(that.stype) : that.stype != null) return false;
        if (sunperformpart != null ? !sunperformpart.equals(that.sunperformpart) : that.sunperformpart != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = iid != null ? iid.hashCode() : 0;
        result = 31 * result + (sstdstg != null ? sstdstg.hashCode() : 0);
        result = 31 * result + (sstdstl != null ? sstdstl.hashCode() : 0);
        result = 31 * result + (dupdateTime != null ? dupdateTime.hashCode() : 0);
        result = 31 * result + (sloc != null ? sloc.hashCode() : 0);
        result = 31 * result + (dlastmod != null ? dlastmod.hashCode() : 0);
        result = 31 * result + (schangefreq != null ? schangefreq.hashCode() : 0);
        result = 31 * result + (spriority != null ? spriority.hashCode() : 0);
        result = 31 * result + (ssitelink != null ? ssitelink.hashCode() : 0);
        result = 31 * result + (siname != null ? siname.hashCode() : 0);
        result = 31 * result + (stype != null ? stype.hashCode() : 0);
        result = 31 * result + (scardnum != null ? scardnum.hashCode() : 0);
        result = 31 * result + (scasecode != null ? scasecode.hashCode() : 0);
        result = 31 * result + (iage != null ? iage.hashCode() : 0);
        result = 31 * result + (ssexy != null ? ssexy.hashCode() : 0);
        result = 31 * result + (sfocusnumber != null ? sfocusnumber.hashCode() : 0);
        result = 31 * result + (sareaname != null ? sareaname.hashCode() : 0);
        result = 31 * result + (sbusinessentity != null ? sbusinessentity.hashCode() : 0);
        result = 31 * result + (scourtname != null ? scourtname.hashCode() : 0);
        result = 31 * result + (sduty != null ? sduty.hashCode() : 0);
        result = 31 * result + (sperformance != null ? sperformance.hashCode() : 0);
        result = 31 * result + (sdisrupttypename != null ? sdisrupttypename.hashCode() : 0);
        result = 31 * result + (dpublishdate != null ? dpublishdate.hashCode() : 0);
        result = 31 * result + (spartytypename != null ? spartytypename.hashCode() : 0);
        result = 31 * result + (sgistid != null ? sgistid.hashCode() : 0);
        result = 31 * result + (dregdate != null ? dregdate.hashCode() : 0);
        result = 31 * result + (sgistunit != null ? sgistunit.hashCode() : 0);
        result = 31 * result + (sperformedpart != null ? sperformedpart.hashCode() : 0);
        result = 31 * result + (sunperformpart != null ? sunperformpart.hashCode() : 0);
        result = 31 * result + (spublishdatestamp != null ? spublishdatestamp.hashCode() : 0);
        result = 31 * result + (ssiteid != null ? ssiteid.hashCode() : 0);
        return result;
    }
}
