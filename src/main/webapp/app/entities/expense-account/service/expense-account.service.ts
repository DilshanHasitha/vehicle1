import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpenseAccount, NewExpenseAccount } from '../expense-account.model';

export type PartialUpdateExpenseAccount = Partial<IExpenseAccount> & Pick<IExpenseAccount, 'id'>;

type RestOf<T extends IExpenseAccount | NewExpenseAccount> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

export type RestExpenseAccount = RestOf<IExpenseAccount>;

export type NewRestExpenseAccount = RestOf<NewExpenseAccount>;

export type PartialUpdateRestExpenseAccount = RestOf<PartialUpdateExpenseAccount>;

export type EntityResponseType = HttpResponse<IExpenseAccount>;
export type EntityArrayResponseType = HttpResponse<IExpenseAccount[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expenseAccount: NewExpenseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseAccount);
    return this.http
      .post<RestExpenseAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(expenseAccount: IExpenseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseAccount);
    return this.http
      .put<RestExpenseAccount>(`${this.resourceUrl}/${this.getExpenseAccountIdentifier(expenseAccount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(expenseAccount: PartialUpdateExpenseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseAccount);
    return this.http
      .patch<RestExpenseAccount>(`${this.resourceUrl}/${this.getExpenseAccountIdentifier(expenseAccount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExpenseAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExpenseAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExpenseAccountIdentifier(expenseAccount: Pick<IExpenseAccount, 'id'>): number {
    return expenseAccount.id;
  }

  compareExpenseAccount(o1: Pick<IExpenseAccount, 'id'> | null, o2: Pick<IExpenseAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseAccountIdentifier(o1) === this.getExpenseAccountIdentifier(o2) : o1 === o2;
  }

  addExpenseAccountToCollectionIfMissing<Type extends Pick<IExpenseAccount, 'id'>>(
    expenseAccountCollection: Type[],
    ...expenseAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseAccounts: Type[] = expenseAccountsToCheck.filter(isPresent);
    if (expenseAccounts.length > 0) {
      const expenseAccountCollectionIdentifiers = expenseAccountCollection.map(
        expenseAccountItem => this.getExpenseAccountIdentifier(expenseAccountItem)!
      );
      const expenseAccountsToAdd = expenseAccounts.filter(expenseAccountItem => {
        const expenseAccountIdentifier = this.getExpenseAccountIdentifier(expenseAccountItem);
        if (expenseAccountCollectionIdentifiers.includes(expenseAccountIdentifier)) {
          return false;
        }
        expenseAccountCollectionIdentifiers.push(expenseAccountIdentifier);
        return true;
      });
      return [...expenseAccountsToAdd, ...expenseAccountCollection];
    }
    return expenseAccountCollection;
  }

  protected convertDateFromClient<T extends IExpenseAccount | NewExpenseAccount | PartialUpdateExpenseAccount>(
    expenseAccount: T
  ): RestOf<T> {
    return {
      ...expenseAccount,
      transactionDate: expenseAccount.transactionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restExpenseAccount: RestExpenseAccount): IExpenseAccount {
    return {
      ...restExpenseAccount,
      transactionDate: restExpenseAccount.transactionDate ? dayjs(restExpenseAccount.transactionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExpenseAccount>): HttpResponse<IExpenseAccount> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExpenseAccount[]>): HttpResponse<IExpenseAccount[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
