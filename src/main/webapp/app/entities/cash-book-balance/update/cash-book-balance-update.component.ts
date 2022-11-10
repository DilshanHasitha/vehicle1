import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CashBookBalanceFormService, CashBookBalanceFormGroup } from './cash-book-balance-form.service';
import { ICashBookBalance } from '../cash-book-balance.model';
import { CashBookBalanceService } from '../service/cash-book-balance.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

@Component({
  selector: 'jhi-cash-book-balance-update',
  templateUrl: './cash-book-balance-update.component.html',
})
export class CashBookBalanceUpdateComponent implements OnInit {
  isSaving = false;
  cashBookBalance: ICashBookBalance | null = null;

  merchantsSharedCollection: IMerchant[] = [];
  transactionTypesSharedCollection: ITransactionType[] = [];

  editForm: CashBookBalanceFormGroup = this.cashBookBalanceFormService.createCashBookBalanceFormGroup();

  constructor(
    protected cashBookBalanceService: CashBookBalanceService,
    protected cashBookBalanceFormService: CashBookBalanceFormService,
    protected merchantService: MerchantService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashBookBalance }) => {
      this.cashBookBalance = cashBookBalance;
      if (cashBookBalance) {
        this.updateForm(cashBookBalance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashBookBalance = this.cashBookBalanceFormService.getCashBookBalance(this.editForm);
    if (cashBookBalance.id !== null) {
      this.subscribeToSaveResponse(this.cashBookBalanceService.update(cashBookBalance));
    } else {
      this.subscribeToSaveResponse(this.cashBookBalanceService.create(cashBookBalance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashBookBalance>>): void {
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

  protected updateForm(cashBookBalance: ICashBookBalance): void {
    this.cashBookBalance = cashBookBalance;
    this.cashBookBalanceFormService.resetForm(this.editForm, cashBookBalance);

    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      cashBookBalance.merchant
    );
    this.transactionTypesSharedCollection = this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
      this.transactionTypesSharedCollection,
      cashBookBalance.transactionType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) =>
          this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.cashBookBalance?.merchant)
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
            this.cashBookBalance?.transactionType
          )
        )
      )
      .subscribe((transactionTypes: ITransactionType[]) => (this.transactionTypesSharedCollection = transactionTypes));
  }
}
