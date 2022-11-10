import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICashBookBalance, NewCashBookBalance } from '../cash-book-balance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICashBookBalance for edit and NewCashBookBalanceFormGroupInput for create.
 */
type CashBookBalanceFormGroupInput = ICashBookBalance | PartialWithRequiredKeyOf<NewCashBookBalance>;

type CashBookBalanceFormDefaults = Pick<NewCashBookBalance, 'id'>;

type CashBookBalanceFormGroupContent = {
  id: FormControl<ICashBookBalance['id'] | NewCashBookBalance['id']>;
  balance: FormControl<ICashBookBalance['balance']>;
  merchant: FormControl<ICashBookBalance['merchant']>;
  transactionType: FormControl<ICashBookBalance['transactionType']>;
};

export type CashBookBalanceFormGroup = FormGroup<CashBookBalanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CashBookBalanceFormService {
  createCashBookBalanceFormGroup(cashBookBalance: CashBookBalanceFormGroupInput = { id: null }): CashBookBalanceFormGroup {
    const cashBookBalanceRawValue = {
      ...this.getFormDefaults(),
      ...cashBookBalance,
    };
    return new FormGroup<CashBookBalanceFormGroupContent>({
      id: new FormControl(
        { value: cashBookBalanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      balance: new FormControl(cashBookBalanceRawValue.balance, {
        validators: [Validators.required],
      }),
      merchant: new FormControl(cashBookBalanceRawValue.merchant),
      transactionType: new FormControl(cashBookBalanceRawValue.transactionType),
    });
  }

  getCashBookBalance(form: CashBookBalanceFormGroup): ICashBookBalance | NewCashBookBalance {
    return form.getRawValue() as ICashBookBalance | NewCashBookBalance;
  }

  resetForm(form: CashBookBalanceFormGroup, cashBookBalance: CashBookBalanceFormGroupInput): void {
    const cashBookBalanceRawValue = { ...this.getFormDefaults(), ...cashBookBalance };
    form.reset(
      {
        ...cashBookBalanceRawValue,
        id: { value: cashBookBalanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CashBookBalanceFormDefaults {
    return {
      id: null,
    };
  }
}
