/*
 * Description: Campo de pesquisa do projeto
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Sat Sep 07 2019 11:31:13 AM
 * version $
 */

import React from 'react';

import './Search.scss';

class SearchProps {}

class SearchState {
  tab!: SearchTab;
  placeholder!: string;
}

enum SearchTab {
  ALL,
  IMAGE,
  AUDIO,
  VIDEO,
}

const commonPlaceholder = 'Digite o que deseja aqui...';

const placeholders: Map<SearchTab, string> = new Map<SearchTab, string>();
placeholders.set(
  SearchTab.ALL,
  `Buscar por todos os arquivos. ${commonPlaceholder}`
);
placeholders.set(SearchTab.IMAGE, `Buscar por imagens. ${commonPlaceholder}`);
placeholders.set(SearchTab.AUDIO, `Buscar por áudios. ${commonPlaceholder}`);
placeholders.set(SearchTab.VIDEO, `Buscar por vídeos. ${commonPlaceholder}`);

class Search extends React.Component<SearchProps, SearchState> {
  componentWillMount() {
    this.activeTab(SearchTab.ALL);
  }

  activeTab(tab: SearchTab) {
    this.setState({
      tab: tab,
      placeholder: placeholders.get(tab) || '',
    });
  }

  isActiveTab(tab: SearchTab): boolean {
    return this.state.tab === tab;
  }

  getClassActiveTab(tab: SearchTab): string {
    return this.isActiveTab(tab) ? 'active' : '';
  }

  render() {
    return (
      <div className="search ui vertical segment">
        <div className="ui secondary pointing menu">
          <button
            type="button"
            className={`item ${this.getClassActiveTab(SearchTab.ALL)}`}
            onClick={() => this.activeTab(SearchTab.ALL)}
          >
            <i className="ui icon file outline" /> Todos
          </button>
          <button
            type="button"
            className={`item ${this.getClassActiveTab(SearchTab.IMAGE)}`}
            onClick={() => this.activeTab(SearchTab.IMAGE)}
          >
            <i className="ui icon file image outline" /> Imagem
          </button>
          <button
            type="button"
            className={`item ${this.getClassActiveTab(SearchTab.AUDIO)}`}
            onClick={() => this.activeTab(SearchTab.AUDIO)}
          >
            <i className="ui icon file audio outline" /> Áudio
          </button>
          <button
            type="button"
            className={`item ${this.getClassActiveTab(SearchTab.VIDEO)}`}
            onClick={() => this.activeTab(SearchTab.VIDEO)}
          >
            <i className="ui icon file video outline" /> Vídeo
          </button>
        </div>
        <div className="ui icon input fluid large">
          <i className="search icon" />
          <input type="text" placeholder={this.state.placeholder} />
        </div>
      </div>
    );
  }
}

export default Search;
