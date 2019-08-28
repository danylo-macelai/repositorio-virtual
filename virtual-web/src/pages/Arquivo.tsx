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

import ArquivoApi from '../api/virtual-master/arquivo-api';

import './Arquivo.scss';

import fileImage from '../assets/images/file.png';

class ArquivoProps {
  alert!: AlertManager;
}

class ArquivoState {
  files!: File[];
  showRemoveFile!: Boolean;
  showOk!: Boolean;
}

class Arquivo extends React.Component<ArquivoProps, ArquivoState> {
  buttonUploadFile: RefObject<HTMLButtonElement>;
  buttonCancelFile: RefObject<HTMLButtonElement>;
  inputUploadFile: RefObject<HTMLInputElement>;

  loadings: RefObject<HTMLSpanElement>;

  constructor(props: ArquivoProps) {
    super(props);

    this.state = {
      files: [],
      showRemoveFile: true,
      showOk: false,
    };

    this.buttonUploadFile = React.createRef();
    this.inputUploadFile = React.createRef();
    this.buttonCancelFile = React.createRef();
    this.loadings = React.createRef();
  }

  async saveFile(event: any) {
    event.preventDefault();

    const input: HTMLInputElement = this.inputUploadFile.current!;
    const button: HTMLButtonElement = this.buttonUploadFile.current!;
    const cancel: HTMLButtonElement = this.buttonCancelFile.current!;

    if (!this.state.files.length) {
      this.props.alert.info(
        'Por favor, selecione pelo menos um arquivo para fazer o upload!'
      );

      return;
    }

    this.setState({
      showRemoveFile: false,
    });

    input.disabled = true;
    button.disabled = true;
    cancel.disabled = true;

    const files = this.state.files;

    for (let i = 0; i < files.length; i++) {
      const file: any = files[i];

      file.loading = true;

      this.setState({
        files: files,
      });

      try {
        const response = await ArquivoApi.gravar(file);
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
      showOk: true,
    });
  }

  ok() {
    const input: HTMLInputElement = this.inputUploadFile.current!;

    input.disabled = false;
    input.value = '';

    this.setState({
      files: [],
      showRemoveFile: true,
      showOk: false,
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
            <li key={file.name} className="item">
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

  renderActionButtons() {
    if (this.state.showOk) {
      return (
        <button
          type="button"
          className="ui button green"
          onClick={event => this.ok()}
        >
          Ok
        </button>
      );
    }

    return (
      <>
        <button
          type="submit"
          className="ui button primary"
          ref={this.buttonUploadFile}
        >
          Salvar
        </button>
        <button
          type="button"
          className="ui button"
          ref={this.buttonCancelFile}
          onClick={event => this.cancelFile()}
        >
          Cancelar
        </button>
      </>
    );
  }

  render() {
    return (
      <div className="upload-file-container">
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
    );
  }
}

export default withAlert()(Arquivo);
