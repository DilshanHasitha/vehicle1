import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeAccountFormService, EmployeeAccountFormGroup } from './employee-account-form.service';
import { IEmployeeAccount } from '../employee-account.model';
import { EmployeeAccountService } from '../service/employee-account.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-employee-account-update',
  templateUrl: './employee-account-update.component.html',
})
export class EmployeeAccountUpdateComponent implements OnInit {
  isSaving = false;
  employeeAccount: IEmployeeAccount | null = null;

  transactionTypesSharedCollection: ITransactionType[] = [];
  merchantsSharedCollection: IMerchant[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: EmployeeAccountFormGroup = this.employeeAccountFormService.createEmployeeAccountFormGroup();

  constructor(
    protected employeeAccountService: EmployeeAccountService,
    protected employeeAccountFormService: EmployeeAccountFormService,
    protected transactionTypeService: TransactionTypeService,
    protected merchantService: MerchantService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeAccount }) => {
      this.employeeAccount = employeeAccount;
      if (employeeAccount) {
        this.updateForm(employeeAccount);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeAccount = this.employeeAccountFormService.getEmployeeAccount(this.editForm);
    if (employeeAccount.id !== null) {
      this.subscribeToSaveResponse(this.employeeAccountService.update(employeeAccount));
    } else {
      this.subscribeToSaveResponse(this.employeeAccountService.create(employeeAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeAccount>>): void {
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

  protected updateForm(employeeAccount: IEmployeeAccount): void {
    this.employeeAccount = employeeAccount;
    this.employeeAccountFormService.resetForm(this.editForm, employeeAccount);

    this.transactionTypesSharedCollection = this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
      this.transactionTypesSharedCollection,
      employeeAccount.transactionType
    );
    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      employeeAccount.merchant
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employeeAccount.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionTypeService
      .query()
      .pipe(map((res: HttpResponse<ITransactionType[]>) => res.body ?? []))
      .pipe(
        map((transactionTypes: ITransactionType[]) =>
          this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
            transactionTypes,
            this.employeeAccount?.transactionType
          )
        )
      )
      .subscribe((transactionTypes: ITransactionType[]) => (this.transactionTypesSharedCollection = transactionTypes));

    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) =>
          this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.employeeAccount?.merchant)
        )
      )
      .subscribe((merchants: IMerchant[]) => (this.merchantsSharedCollection = merchants));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employeeAccount?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
