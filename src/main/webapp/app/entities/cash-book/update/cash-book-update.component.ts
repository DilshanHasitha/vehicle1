import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CashBookFormService, CashBookFormGroup } from './cash-book-form.service';
import { ICashBook } from '../cash-book.model';
import { CashBookService } from '../service/cash-book.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

@Component({
  selector: 'jhi-cash-book-update',
  templateUrl: './cash-book-update.component.html',
})
export class CashBookUpdateComponent implements OnInit {
  isSaving = false;
  cashBook: ICashBook | null = null;

  merchantsSharedCollection: IMerchant[] = [];
  transactionTypesSharedCollection: ITransactionType[] = [];

  editForm: CashBookFormGroup = this.cashBookFormService.createCashBookFormGroup();

  constructor(
    protected cashBookService: CashBookService,
    protected cashBookFormService: CashBookFormService,
    protected merchantService: MerchantService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashBook }) => {
      this.cashBook = cashBook;
      if (cashBook) {
        this.updateForm(cashBook);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashBook = this.cashBookFormService.getCashBook(this.editForm);
    if (cashBook.id !== null) {
      this.subscribeToSaveResponse(this.cashBookService.update(cashBook));
    } else {
      this.subscribeToSaveResponse(this.cashBookService.create(cashBook));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashBook>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cashBook: ICashBook): void {
    this.cashBook = cashBook;
    this.cashBookFormService.resetForm(this.editForm, cashBook);

    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      cashBook.merchant
    );
    this.transactionTypesSharedCollection = this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
      this.transactionTypesSharedCollection,
      cashBook.transactionType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) =>
          this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.cashBook?.merchant)
        )
      )
      .subscribe((merchants: IMerchant[]) => (this.merchantsSharedCollection = merchants));

    this.transactionTypeService
      .query()
      .pipe(map((res: HttpResponse<ITransactionType[]>) => res.body ?? []))
      .pipe(
        map((transactionTypes: ITransactionType[]) =>
          this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
            transactionTypes,
            this.cashBook?.transactionType
          )
        )
      )
      .subscribe((transactionTypes: ITransactionType[]) => (this.transactionTypesSharedCollection = transactionTypes));
  }
}
