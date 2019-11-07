/*
 * Description: Pagina para fazer upload de arquivos
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Mon Aug 05 2019 12:58:24 PM
 * version $
 */

import React, { RefObject } from 'react';
import { withAlert, AlertManager } from 'react-alert';
import { connect } from 'react-redux';

import fileImage from '../assets/images/file.png';
import { gravacaoArquivo } from '../services/servicosApi';

import './scss/ArquivoUpload.scss';
import { VirtualWebState } from '../reducers';
import { Redirect } from 'react-router';

const mapStateToProps = (state: VirtualWebState) => {
  return {
    user: state.user,
  };
};

interface ArquivoUploadProps {
  alert: AlertManager;
}

interface ArquivoUploadState {
  files: File[];
  showRemoveFile: Boolean;
  showSave: Boolean;
  showCancel: Boolean;
  showClear: Boolean;
  showRetry: Boolean;
}

type AllArquivoUploadProps = ArquivoUploadProps &
  ReturnType<typeof mapStateToProps>;

class ArquivoUploadPage extends React.Component<
  AllArquivoUploadProps,
  ArquivoUploadState
> {
  inputUploadFile: RefObject<HTMLInputElement>;

  loadings: RefObject<HTMLSpanElement>;

  constructor(props: AllArquivoUploadProps) {
    super(props);

    this.state = {
      files: [],
      showSave: true,
      showCancel: true,
      showRemoveFile: true,
      showClear: false,
      showRetry: false,
    };

    this.inputUploadFile = React.createRef();
    this.loadings = React.createRef();
  }

  async saveFile(event: any) {
    event.preventDefault();

    const input: HTMLInputElement = this.inputUploadFile.current!;

    if (!this.state.files.length) {
      this.props.alert.info(
        'Por favor, selecione pelo menos um arquivo para fazer o upload!'
      );

      return;
    }

    this.setState({
      showRemoveFile: false,
      showSave: false,
      showCancel: false,
    });

    input.disabled = true;

    const files = this.state.files;

    for (let i = 0; i < files.length; i++) {
      const file: any = files[i];

      file.loading = true;

      this.setState({
        files: files,
      });

      try {
        const response = await gravacaoArquivo(file, this.props.user.token);
        console.log(response);
        file.success = true;
      } catch (e) {
        file.error = true;
      }

      file.loading = false;

      this.setState({
        files: files,
      });
    }

    this.setState({
      showClear: true,
      showRetry: files.some((f: any) => f.error),
    });
  }

  async retry() {
    this.setState({
      showClear: false,
      showRetry: false,
    });

    const filesSuccess = this.state!.files.filter((f: any) => !f.error);
    const files = this.state.files.filter((f: any) => f.error);

    for (let i = 0; i < files.length; i++) {
      const file: any = files[i];

      file.loading = true;
      file.error = false;

      this.setState({
        files: files,
      });

      try {
        const response = await gravacaoArquivo(file, this.props.user.token);
        console.log(response);
        file.success = true;
      } catch (e) {
        file.error = true;
      }

      file.loading = false;

      this.setState({
        files: [...filesSuccess, ...files],
      });
    }

    this.setState({
      showClear: true,
      showRetry: files.some((f: any) => f.error),
    });
  }

  clear() {
    const input: HTMLInputElement = this.inputUploadFile.current!;

    input.disabled = false;
    input.value = '';

    this.setState({
      files: [],
      showRemoveFile: true,
      showSave: true,
      showCancel: true,
      showClear: false,
      showRetry: false,
    });
  }

  openFileInput() {
    if (this.inputUploadFile.current) {
      this.inputUploadFile.current.click();
    }
  }

  changeInputFile(event: any) {
    const files: FileList = event.target.files;

    const filesToAdd = [];

    for (let i = 0; i < files.length; i++) {
      const file: any = files.item(i)!;

      const exists = this.state.files.some(f => f.name === file.name);

      if (!exists) {
        if (file.type.match(/^image/gi)) {
          file.url = URL.createObjectURL(file);
        }

        filesToAdd.unshift(file);
      } else {
        this.props.alert.error(`Existe um arquivo com este nome: ${file.name}`);
      }
    }

    this.setState({
      files: [...filesToAdd, ...this.state.files],
    });

    event.target.value = null;
  }

  removeFile(index: Number) {
    this.setState({
      files: this.state.files.filter((file, i) => i !== index),
    });
  }

  cancelFile() {
    this.setState({
      files: [],
    });
  }

  renderPedingIcon(file: any) {
    if (this.state.showRemoveFile || file.loading !== undefined) {
      return <></>;
    }

    return <i className="clock icon" title="Pendente" />;
  }

  renderSuccessIcon(file: any) {
    if (!file.success) {
      return <></>;
    }

    return <i className="check icon green" title="Enviado" />;
  }

  renderErrorIcon(file: any) {
    if (!file.error) {
      return <></>;
    }

    return <i className="times icon red" title="Falha ao enviar o arquivo" />;
  }

  renderLoadingIcon(file: any) {
    if (!file.loading) {
      return <></>;
    }

    return <span className="ui active inline tiny loader" title="Carregando" />;
  }

  renderFileRemoveButton(index: Number) {
    if (!this.state.showRemoveFile) {
      return <></>;
    }
    return (
      <button
        className="ui button tiny red"
        onClick={() => this.removeFile(index)}
      >
        Remover
      </button>
    );
  }

  renderFilesContainer() {
    if (!this.state.files.length) {
      return <></>;
    }

    return (
      <div>
        <h3 className="ui header">Arquivos</h3>
        <ul className="ui divided items">
          {this.state.files.map((file: any, index: Number) => (
            <li key={file.name} className={`item ${file.error ? 'error' : ''}`}>
              <div className="ui custom image">
                <img src={file.url || fileImage} alt=" " />
              </div>
              <div className="middle aligned content">
                <span className="title-image">{file.name}</span>
                <div className="ui right floated">
                  {this.renderPedingIcon(file)}

                  {this.renderSuccessIcon(file)}

                  {this.renderErrorIcon(file)}

                  {this.renderLoadingIcon(file)}

                  {this.renderFileRemoveButton(index)}
                </div>
              </div>
            </li>
          ))}
        </ul>
      </div>
    );
  }

  renderActionButtonSave() {
    if (!this.state.showSave) {
      return <></>;
    }

    return (
      <button type="submit" className="ui button primary">
        Salvar
      </button>
    );
  }

  renderActionButtonCancel() {
    if (!this.state.showCancel) {
      return <></>;
    }

    return (
      <button
        type="button"
        className="ui button basic"
        onClick={event => this.cancelFile()}
      >
        Cancelar
      </button>
    );
  }

  renderActionButtonClear() {
    if (!this.state.showClear) {
      return <></>;
    }

    return (
      <button
        type="button"
        className={`ui button ${this.state.showRetry ? 'basic' : 'green'}`}
        onClick={event => this.clear()}
      >
        {this.state.showRetry ? 'Não, obrigado!' : 'Ok, Obrigado!'}
      </button>
    );
  }

  renderActionButtonRetry() {
    if (!this.state.showRetry) {
      return <></>;
    }

    return (
      <button
        type="button"
        className="ui button green"
        onClick={event => this.retry()}
      >
        Sim, tentar novamente!
      </button>
    );
  }

  renderFileFailAlert() {
    if (!this.state.showRetry) {
      return <></>;
    }
    return (
      <div className="upload-file-fail-alert">
        Ocorreu uma falha ao enviar alguns arquivos. Deseja tentar novamente?
      </div>
    );
  }

  renderActionButtons() {
    return (
      <>
        {this.renderFileFailAlert()}
        {this.renderActionButtonSave()}
        {this.renderActionButtonCancel()}
        {this.renderActionButtonRetry()}
        {this.renderActionButtonClear()}
      </>
    );
  }

  render() {
    if (!this.props.user || !this.props.user.token) {
      return <Redirect to="/login" />;
    }

    return (
      <div className="upload-file-container ui grid centered">
        <div className="column ten wide">
          <h2 className="ui header">Adicionar arquivo</h2>
          <form className="ui form" onSubmit={event => this.saveFile(event)}>
            <div className="field">
              <div className="upload-file-handle">
                <input
                  type="file"
                  name="file"
                  ref={this.inputUploadFile}
                  onChange={event => this.changeInputFile(event)}
                  multiple
                />
                <label onClick={event => this.openFileInput()}>
                  Clique aqui para adicionar um arquivo
                </label>
              </div>
            </div>

            {this.renderFilesContainer()}

            <div className="ui hidden divider" />

            {this.renderActionButtons()}
          </form>
        </div>
      </div>
    );
  }
}

export default withAlert()(connect(mapStateToProps)(ArquivoUploadPage));
