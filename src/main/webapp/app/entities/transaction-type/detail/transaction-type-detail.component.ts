import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionType } from '../transaction-type.model';

@Component({
  selector: 'jhi-transaction-type-detail',
  templateUrl: './transaction-type-detail.component.html',
})
export class TransactionTypeDetailComponent implements OnInit {
  transactionType: ITransactionType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionType }) => {
      this.transactionType = transactionType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
