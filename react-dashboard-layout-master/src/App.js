import React from 'react';
import BasicLayout from './layout/BasicLayout';
import Home from './pages/Home';
import Languages from './pages/Languages';
import { ViewState } from './core/enum';
import Translations from './pages/Translations';
import { message } from 'antd';
import loginLogo from "./assets/logo.png";
import { request } from './axios/request';
import LoadingIndicator from './common/LoadingIndicator';
import { connect } from 'react-redux';
import { updateAuthData } from './actions/authActions';
const ACCESS_TOKEN = "accessToken";

const pages = [
  {
    key: 'home',
    component: Home
  },
  {
    key: 'languages',
    component: Languages
  },
  {
    key: 'translations',
    component: Translations,
    componentProps: { mainText: 'This is main text for component' }
  }
];
const menuItems = [
  {
    key: 'home',
    value: 'Home',
    icon: 'home',
    rules: {}
  },
  {
    key: 'localization',
    value: 'Localization',
    icon: 'zhihu',
    subItems: [
      {
        key: 'languages',
        icon: 'insurance',
        value: 'Languages',
        rules: {
          myButton: ViewState.DISABLED
        }
      },
      {
        key: 'translations',
        icon: 'read',
        value: 'Translations',
      }
    ]
  }
];
const applicationName = "Management App";
class App extends React.Component {

  constructor(props) {
    super(props);
  }

  onLogin = credentials => {
    this.props.updateAuthData(
      {
        user: {
          firstName: 'Djordje',
          lastName: 'Turjacanin'
        },
        dropdownItems: [
          { key: 'logout', icon: 'logout', value: 'Logout', callback: () => { this.setState({ authenticated: false }); console.log("AAAA"); localStorage.removeItem(ACCESS_TOKEN); } }
        ],
        authenticated: true
      }
    )
    message.success('Login successful.');
  };


  onLogout = () => {
    console.log("on logout");
    this.updateAuthData({ authenticated: false, dropdownItems: [], user: null });
    localStorage.removeItem(ACCESS_TOKEN);
  }


  getCurrentUser() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
      return Promise.reject("No access token set.");
    }

    return request.get("/user/me");
  }

  loadCurrentUser() {
    if (localStorage.getItem(ACCESS_TOKEN)) {
      this.props.updateAuthData({
        isLoading: true
      });
      this.getCurrentUser().then(response => {
        console.log(response);
        this.props.updateAuthData({
          user: response.data,
          authenticated: true,
          dropdownItems: [
            { key: 'logout', icon: 'logout', value: 'Logout', callback: () => { this.setState({ authenticated: false }); localStorage.removeItem(ACCESS_TOKEN); } }
          ],
          isLoading: false
        });
      }).catch(error => {
        this.props.updateAuthData({
          isLoading: false
        });
      });

    }
  }

  componentDidMount() {
    this.loadCurrentUser();

    request.interceptors.response.use(resp => {
      return resp;
    }, error => {
      let res = error.response;
      if (409 === error.response.status) {
        if (res.data) {
          localStorage.setItem(ACCESS_TOKEN, res.data.token);
        }
        error.config.headers.Authorization = "Bearer " + localStorage.getItem(ACCESS_TOKEN);
        return request.request(error.config);
      }
      else if (406 === error.response.status) {
        alert("Both access and refresh token expired");
        localStorage.removeItem(ACCESS_TOKEN);
        this.props.updateAuthData({ user: null, authenticated: false, isLoading: false, dropdownItems: [] });
        return Promise.reject(error);
      }
      else if (403 === error.response.status) {
        alert("You are not authorized");
        return Promise.reject(error);
      }
    })
  }

  render() {
    if (this.props.auth.isLoading) {
      return <LoadingIndicator />
    }
    //const { dropdownItems, user, authenticated } = this.state;
    return (
      <BasicLayout
        applicationName={applicationName}
        menuItems={menuItems}
        pages={pages}
        dropdownItems={this.props.auth.dropdownItems}
        user={this.props.auth.user}
        headerComponents={<span>Hello </span>}
        authenticated={this.props.auth.authenticated}
        onLogin={this.onLogin}
        loginLogo={loginLogo}
      //loginLogoStyle={{top:0,right:0}}
      //footerStyle={{ textAlign: 'center' }}
      //footer={<div>This is footer</div>}
      // forbidden={Forbidden}
      // notFound={NotFound}
      />
    );
  }
}

const mapStateToProps = (state /*, ownProps*/) => {
  return {
    auth: state.auth
  }
}

const mapDispatchToProps = { updateAuthData };

export default connect(mapStateToProps, mapDispatchToProps)(App);
