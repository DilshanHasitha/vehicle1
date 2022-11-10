import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashBook } from '../cash-book.model';

@Component({
  selector: 'jhi-cash-book-detail',
  templateUrl: './cash-book-detail.component.html',
})
export class CashBookDetailComponent implements OnInit {
  cashBook: ICashBook | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashBook }) => {
      this.cashBook = cashBook;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
