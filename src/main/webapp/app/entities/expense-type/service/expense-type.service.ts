import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpenseType, NewExpenseType } from '../expense-type.model';

export type PartialUpdateExpenseType = Partial<IExpenseType> & Pick<IExpenseType, 'id'>;

export type EntityResponseType = HttpResponse<IExpenseType>;
export type EntityArrayResponseType = HttpResponse<IExpenseType[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expenseType: NewExpenseType): Observable<EntityResponseType> {
    return this.http.post<IExpenseType>(this.resourceUrl, expenseType, { observe: 'response' });
  }

  update(expenseType: IExpenseType): Observable<EntityResponseType> {
    return this.http.put<IExpenseType>(`${this.resourceUrl}/${this.getExpenseTypeIdentifier(expenseType)}`, expenseType, {
      observe: 'response',
    });
  }

  partialUpdate(expenseType: PartialUpdateExpenseType): Observable<EntityResponseType> {
    return this.http.patch<IExpenseType>(`${this.resourceUrl}/${this.getExpenseTypeIdentifier(expenseType)}`, expenseType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExpenseType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpenseType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExpenseTypeIdentifier(expenseType: Pick<IExpenseType, 'id'>): number {
    return expenseType.id;
  }

  compareExpenseType(o1: Pick<IExpenseType, 'id'> | null, o2: Pick<IExpenseType, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseTypeIdentifier(o1) === this.getExpenseTypeIdentifier(o2) : o1 === o2;
  }

  addExpenseTypeToCollectionIfMissing<Type extends Pick<IExpenseType, 'id'>>(
    expenseTypeCollection: Type[],
    ...expenseTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseTypes: Type[] = expenseTypesToCheck.filter(isPresent);
    if (expenseTypes.length > 0) {
      const expenseTypeCollectionIdentifiers = expenseTypeCollection.map(
        expenseTypeItem => this.getExpenseTypeIdentifier(expenseTypeItem)!
      );
      const expenseTypesToAdd = expenseTypes.filter(expenseTypeItem => {
        const expenseTypeIdentifier = this.getExpenseTypeIdentifier(expenseTypeItem);
        if (expenseTypeCollectionIdentifiers.includes(expenseTypeIdentifier)) {
          return false;
        }
        expenseTypeCollectionIdentifiers.push(expenseTypeIdentifier);
        return true;
      });
      return [...expenseTypesToAdd, ...expenseTypeCollection];
    }
    return expenseTypeCollection;
  }
}
