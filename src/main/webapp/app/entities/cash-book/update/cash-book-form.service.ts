import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICashBook, NewCashBook } from '../cash-book.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICashBook for edit and NewCashBookFormGroupInput for create.
 */
type CashBookFormGroupInput = ICashBook | PartialWithRequiredKeyOf<NewCashBook>;

type CashBookFormDefaults = Pick<NewCashBook, 'id'>;

type CashBookFormGroupContent = {
  id: FormControl<ICashBook['id'] | NewCashBook['id']>;
  transactionDate: FormControl<ICashBook['transactionDate']>;
  transactionDescription: FormControl<ICashBook['transactionDescription']>;
  transactionAmountDR: FormControl<ICashBook['transactionAmountDR']>;
  transactionAmountCR: FormControl<ICashBook['transactionAmountCR']>;
  transactionBalance: FormControl<ICashBook['transactionBalance']>;
  merchant: FormControl<ICashBook['merchant']>;
  transactionType: FormControl<ICashBook['transactionType']>;
};

export type CashBookFormGroup = FormGroup<CashBookFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CashBookFormService {
  createCashBookFormGroup(cashBook: CashBookFormGroupInput = { id: null }): CashBookFormGroup {
    const cashBookRawValue = {
      ...this.getFormDefaults(),
      ...cashBook,
    };
    return new FormGroup<CashBookFormGroupContent>({
      id: new FormControl(
        { value: cashBookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      transactionDate: new FormControl(cashBookRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      transactionDescription: new FormControl(cashBookRawValue.transactionDescription, {
        validators: [Validators.required],
      }),
      transactionAmountDR: new FormControl(cashBookRawValue.transactionAmountDR, {
        validators: [Validators.required],
      }),
      transactionAmountCR: new FormControl(cashBookRawValue.transactionAmountCR, {
        validators: [Validators.required],
      }),
      transactionBalance: new FormControl(cashBookRawValue.transactionBalance, {
        validators: [Validators.required],
      }),
      merchant: new FormControl(cashBookRawValue.merchant),
      transactionType: new FormControl(cashBookRawValue.transactionType),
    });
  }

  getCashBook(form: CashBookFormGroup): ICashBook | NewCashBook {
    return form.getRawValue() as ICashBook | NewCashBook;
  }

  resetForm(form: CashBookFormGroup, cashBook: CashBookFormGroupInput): void {
    const cashBookRawValue = { ...this.getFormDefaults(), ...cashBook };
    form.reset(
      {
        ...cashBookRawValue,
        id: { value: cashBookRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CashBookFormDefaults {
    return {
      id: null,
    };
  }
}
