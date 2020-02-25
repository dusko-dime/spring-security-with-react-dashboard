import React from 'react';
import { Form, Button, Input, Icon, message } from 'antd';
import "./index.css";
import { request } from  "../../axios/request";
const ACCESS_TOKEN = "accessToken";

class Login extends React.Component {
  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {

        this.props.form.validateFields((err, values) => {
          if (!err) {
            const loginRequest = Object.assign({}, values);
            request.post('/auth/signin', loginRequest)
              .then(response => {
                localStorage.setItem(ACCESS_TOKEN, response.data.accessToken);
                console.log(response.data.accessToken);
                this.props.onLogin(response.data);
              }).catch(error => {
                if (error.status === 401) {
                  message.error({
                    message: 'App',
                    description: 'Your Username or Password is incorrect. Please try again!'
                  });
                } else {
                  message.error({
                    message: 'App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                  });
                }
              });
          }
        });
      }
    });
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    const { logo, loginLogoStyle } = this.props;
    return (
      <div className="login-panel">
        {logo && <img src={logo} alt="logo" className="login-logo" style={loginLogoStyle} />}
        <Form onSubmit={this.handleSubmit} className="login-form">
          <Form.Item>
            {getFieldDecorator("usernameOrEmail", {
              rules: [{ required: true, message: "Username is mandatory!" }]
            })(
              <Input
                prefix={<Icon type="user" style={{ color: "rgba(0,0,0,.25)" }} />}
                placeholder="usernameOrEmail"
              />
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator("password", {
              rules: [{ required: true, message: "Password is mandatory!" }]
            })(
              <Input
                prefix={<Icon type="lock" style={{ color: "rgba(0,0,0,.25)" }} />}
                type="password"
                placeholder="Password"
              />
            )}
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
            >
              Login
              </Button>
          </Form.Item>
        </Form>
      </div>);
  }
}

export default Form.create({})(Login);