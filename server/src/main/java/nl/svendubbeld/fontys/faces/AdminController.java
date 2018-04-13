package nl.svendubbeld.fontys.faces;

import nl.svendubbeld.fontys.dto.SecurityGroupDTO;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.dto.UserDTOSecure;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Named("adminController")
@ViewScoped
public class AdminController implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Inject
    private UserService userService;

    @Inject
    private SecurityService securityService;

    @Inject
    private TweetService tweetService;

    private List<UserDTOSecure> users;
    private DataModel<UserDTOSecure> userModel;

    private List<SecurityGroupDTO> securityGroups;

    private List<TweetDTO> tweets;
    private DataModel<TweetDTO> tweetModel;

    @PostConstruct
    public void init() {
        users = userService.findAllAsDTOSecure();
        userModel = new ListDataModel<>(users);

        securityGroups = securityService.findAllGroupsAsDTO();

        tweets = tweetService.findAllAsDTO();
        tweetModel = new ListDataModel<>(tweets);
    }


    public DataModel<UserDTOSecure> getUserModel() {
        return userModel;
    }

    public List<SecurityGroupDTO> getSecurityGroupList() {
        return securityGroups;
    }

    public DataModel<TweetDTO> getTweetModel() {
        return tweetModel;
    }

    public void securityGroupsChanged(ValueChangeEvent e) {
        UserDTOSecure user = userModel.getRowData();
        user.setSecurityGroups((Set<SecurityGroupDTO>) e.getNewValue());

        userService.editSecurityGroups(user);
    }

    public void deleteTweet(ActionEvent event) {
        TweetDTO tweet = tweetModel.getRowData();

        tweetService.remove(tweet);
        tweets.remove(tweet);
    }

    public void logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();

            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
        } catch (ServletException e) {
            logger.error("Logout failed", e);
        }

        context.getExternalContext().redirect("/admin/");
    }
}
