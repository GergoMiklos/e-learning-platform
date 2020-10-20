import React from 'react';
import {cleanup, render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom'
import GroupListElementComp from "./GroupListElementComp";
import {formatShortDate} from "../../utils/date-utils";

jest.mock('../../utils/date-utils');

const onClickPath = '/clicked';
const mockDateString = '1995-12-17T03:24:00';
const mockGroup = {id: 2, name: 'Group1', news: 'News1', newsChangedDate: new Date(mockDateString)};

describe('GroupListElementComponent', () => {

    afterEach(() => {
        jest.clearAllMocks();
        cleanup();
    });

    test('shows group name and fresh news changed time', async () => {
        formatShortDate.mockReturnValue(mockDateString);
        let props = {
            group: mockGroup,
            onClickPath: onClickPath,
            isNewsFresh: true,
        }

        render(
            <GroupListElementComp {...props}/>, {wrapper: MemoryRouter}
        );

        expect(await screen.findByRole('link')).toHaveAttribute('href', onClickPath);
        expect(screen.getByText(mockGroup.name)).toBeInTheDocument();
        expect(screen.getByText(mockDateString)).toBeInTheDocument();

    });

    test('does not show not fresh news changed time', async () => {
        formatShortDate.mockReturnValue(mockDateString);
        let props = {
            group: mockGroup,
            onClickPath: onClickPath,
            isNewsFresh: false,
        }

        render(
            <GroupListElementComp {...props}/>, {wrapper: MemoryRouter}
        );

        expect(await screen.findByRole('link')).toHaveAttribute('href', onClickPath);
        expect(screen.getByText(mockGroup.name)).toBeInTheDocument();
        expect(screen.queryByText(mockDateString)).not.toBeInTheDocument();

    });

});