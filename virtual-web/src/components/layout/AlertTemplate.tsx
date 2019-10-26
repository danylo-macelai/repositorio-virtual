/*
 * Description: Componente de Template do Alert da aplicacao
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Aug 07 2019 11:19:00 PM
 * version $
 */

import React from 'react';
import { AlertComponentPropsWithStyle } from 'react-alert';

class AlertTemplate extends React.Component<AlertComponentPropsWithStyle, any> {
  render() {
    return (
      <div
        className={`ui message ${this.props.options.type}`}
        style={{ ...this.props.style, minWidth: 350, maxWidth: 500 }}
      >
        <p style={{ margin: '0px 12px 0 0' }}>{this.props.message}</p>
        <i className="close icon" onClick={this.props.close} />
      </div>
    );
  }
}

export default AlertTemplate;
