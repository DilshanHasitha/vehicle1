import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ExpenseAccountBalanceFormService, ExpenseAccountBalanceFormGroup } from './expense-account-balance-form.service';
import { IExpenseAccountBalance } from '../expense-account-balance.model';
import { ExpenseAccountBalanceService } from '../service/expense-account-balance.service';
import { IExpense } from 'app/entities/expense/expense.model';
import { ExpenseService } from 'app/entities/expense/service/expense.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

@Component({
  selector: 'jhi-expense-account-balance-update',
  templateUrl: './expense-account-balance-update.component.html',
})
export class ExpenseAccountBalanceUpdateComponent implements OnInit {
  isSaving = false;
  expenseAccountBalance: IExpenseAccountBalance | null = null;

  expensesSharedCollection: IExpense[] = [];
  merchantsSharedCollection: IMerchant[] = [];
  transactionTypesSharedCollection: ITransactionType[] = [];

  editForm: ExpenseAccountBalanceFormGroup = this.expenseAccountBalanceFormService.createExpenseAccountBalanceFormGroup();

  constructor(
    protected expenseAccountBalanceService: ExpenseAccountBalanceService,
    protected expenseAccountBalanceFormService: ExpenseAccountBalanceFormService,
    protected expenseService: ExpenseService,
    protected merchantService: MerchantService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareExpense = (o1: IExpense | null, o2: IExpense | null): boolean => this.expenseService.compareExpense(o1, o2);

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseAccountBalance }) => {
      this.expenseAccountBalance = expenseAccountBalance;
      if (expenseAccountBalance) {
        this.updateForm(expenseAccountBalance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseAccountBalance = this.expenseAccountBalanceFormService.getExpenseAccountBalance(this.editForm);
    if (expenseAccountBalance.id !== null) {
      this.subscribeToSaveResponse(this.expenseAccountBalanceService.update(expenseAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.expenseAccountBalanceService.create(expenseAccountBalance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseAccountBalance>>): void {
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

  protected updateForm(expenseAccountBalance: IExpenseAccountBalance): void {
    this.expenseAccountBalance = expenseAccountBalance;
    this.expenseAccountBalanceFormService.resetForm(this.editForm, expenseAccountBalance);

    this.expensesSharedCollection = this.expenseService.addExpenseToCollectionIfMissing<IExpense>(
      this.expensesSharedCollection,
      expenseAccountBalance.expense
    );
    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      expenseAccountBalance.merchant
    );
    this.transactionTypesSharedCollection = this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
      this.transactionTypesSharedCollection,
      expenseAccountBalance.transactionType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.expenseService
      .query()
      .pipe(map((res: HttpResponse<IExpense[]>) => res.body ?? []))
      .pipe(
        map((expenses: IExpense[]) =>
          this.expenseService.addExpenseToCollectionIfMissing<IExpense>(expenses, this.expenseAccountBalance?.expense)
        )
      )
      .subscribe((expenses: IExpense[]) => (this.expensesSharedCollection = expenses));

    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) =>
          this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.expenseAccountBalance?.merchant)
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
            this.expenseAccountBalance?.transactionType
          )
        )
      )
      .subscribe((transactionTypes: ITransactionType[]) => (this.transactionTypesSharedCollection = transactionTypes));
  }
}
