import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransactionType, NewTransactionType } from '../transaction-type.model';

export type PartialUpdateTransactionType = Partial<ITransactionType> & Pick<ITransactionType, 'id'>;

export type EntityResponseType = HttpResponse<ITransactionType>;
export type EntityArrayResponseType = HttpResponse<ITransactionType[]>;

@Injectable({ providedIn: 'root' })
export class TransactionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transaction-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transactionType: NewTransactionType): Observable<EntityResponseType> {
    return this.http.post<ITransactionType>(this.resourceUrl, transactionType, { observe: 'response' });
  }

  update(transactionType: ITransactionType): Observable<EntityResponseType> {
    return this.http.put<ITransactionType>(`${this.resourceUrl}/${this.getTransactionTypeIdentifier(transactionType)}`, transactionType, {
      observe: 'response',
    });
  }

  partialUpdate(transactionType: PartialUpdateTransactionType): Observable<EntityResponseType> {
    return this.http.patch<ITransactionType>(`${this.resourceUrl}/${this.getTransactionTypeIdentifier(transactionType)}`, transactionType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransactionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransactionTypeIdentifier(transactionType: Pick<ITransactionType, 'id'>): number {
    return transactionType.id;
  }

  compareTransactionType(o1: Pick<ITransactionType, 'id'> | null, o2: Pick<ITransactionType, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransactionTypeIdentifier(o1) === this.getTransactionTypeIdentifier(o2) : o1 === o2;
  }

  addTransactionTypeToCollectionIfMissing<Type extends Pick<ITransactionType, 'id'>>(
    transactionTypeCollection: Type[],
    ...transactionTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transactionTypes: Type[] = transactionTypesToCheck.filter(isPresent);
    if (transactionTypes.length > 0) {
      const transactionTypeCollectionIdentifiers = transactionTypeCollection.map(
        transactionTypeItem => this.getTransactionTypeIdentifier(transactionTypeItem)!
      );
      const transactionTypesToAdd = transactionTypes.filter(transactionTypeItem => {
        const transactionTypeIdentifier = this.getTransactionTypeIdentifier(transactionTypeItem);
        if (transactionTypeCollectionIdentifiers.includes(transactionTypeIdentifier)) {
          return false;
        }
        transactionTypeCollectionIdentifiers.push(transactionTypeIdentifier);
        return true;
      });
      return [...transactionTypesToAdd, ...transactionTypeCollection];
    }
    return transactionTypeCollection;
  }
}
