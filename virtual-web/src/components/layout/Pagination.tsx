/*
 * Description: Componente de paginacao
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Tue Nov 12 2019 9:14:27 PM
 * version $
 */
import React from 'react';

import './scss/Pagination.scss';
import {
  PAGINATION_MAX_PAGE,
  PAGINATION_MIDDLE_PAGE,
} from '../../common/constants';

interface PaginationProps {
  page: number;
  totalPages: number;
  onSelectPage: (page: number) => void;
}

interface PaginationState {
  startPage: number;
}

class Pagination extends React.Component<PaginationProps, PaginationState> {
  constructor(props: PaginationProps) {
    super(props);

    this.state = {
      startPage: 0,
    };
  }

  isFirstPage(): boolean {
    return this.props.page === 0;
  }

  isLastPage(): boolean {
    return this.props.page === this.props.totalPages - 1;
  }

  setStartPage(page: number) {
    let startPage = 0;

    if (this.props.totalPages > PAGINATION_MAX_PAGE) {
      if (
        page >= PAGINATION_MAX_PAGE - PAGINATION_MIDDLE_PAGE &&
        page + PAGINATION_MIDDLE_PAGE < this.props.totalPages
      ) {
        startPage = page - PAGINATION_MIDDLE_PAGE;
      } else if (page + PAGINATION_MIDDLE_PAGE >= this.props.totalPages) {
        startPage = this.props.totalPages - PAGINATION_MAX_PAGE;
      }
    }

    this.setState({
      startPage: startPage,
    });
  }

  handleSelectPage(page: number) {
    if (this.props.page !== page) {
      this.setStartPage(page);
      this.props.onSelectPage(page);
    }
  }

  handlePrevPage() {
    if (!this.isFirstPage()) {
      this.handleSelectPage(this.props.page - 1);
    }
  }

  handleNextPage() {
    if (!this.isLastPage()) {
      this.handleSelectPage(this.props.page + 1);
    }
  }

  renderPages(totalPages: number) {
    const maxPage =
      totalPages > PAGINATION_MAX_PAGE ? PAGINATION_MAX_PAGE : totalPages;

    const pages = [];
    for (
      let i = this.state.startPage;
      i < maxPage + this.state.startPage;
      i++
    ) {
      pages.push(
        <a
          className={`item ${this.props.page === i ? 'active' : ''}`}
          onClick={() => this.handleSelectPage(i)}
          key={i}
        >
          {i + 1}
        </a>
      );
    }

    return pages;
  }

  render() {
    const { totalPages } = this.props;

    if (totalPages <= 1) {
      return <></>;
    }

    return (
      <div className="pagination">
        <div className="ui pagination menu center">
          <a
            className={`item ${this.isFirstPage() ? 'disabled' : ''}`}
            onClick={() => this.handlePrevPage()}
          >
            <i className="chevron left icon"></i>
          </a>

          {this.renderPages(totalPages)}

          <a
            className={`item ${this.isLastPage() ? 'disabled' : ''}`}
            onClick={() => this.handleNextPage()}
          >
            <i className="chevron right icon"></i>
          </a>
        </div>
      </div>
    );
  }
}

export default Pagination;
